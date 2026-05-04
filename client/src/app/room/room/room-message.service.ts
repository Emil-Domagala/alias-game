import { Injectable, inject, signal } from '@angular/core';
import { StompSubscription } from '@stomp/stompjs';
import { WebSocketService } from '../../web-socket.service';
import {
  ChatMessageWithState,
  MessageRequest,
  MessageSendConfirmation,
  MessageStatus,
  ConversationType,
  MessageType,
} from '../../shared/message.interface';
import {Player} from '../../shared/player.interface';
import {RoomService} from '../room.service';
import {UserService} from '../../user/user.service';

@Injectable({ providedIn: 'root' })
export class RoomMessageService {
  private ws = inject(WebSocketService);
  private roomService = inject(RoomService);
  private userService = inject(UserService);
  private roomSub?: StompSubscription;
  private userSub?: StompSubscription;

  /** Reactive list of messages */
  messages = signal<ChatMessageWithState[]>([]);

  /**
   * Connects to WebSocket for room messages and user-specific queue
   * @param roomId current room ID
   * @param currentUserId current user ID
   */
  connect(roomId: string, currentUserId: string) {
    this.ws.connect(() => {
      // Subscribe to room messages
      this.roomSub = this.ws.subscribe(`/topic/room/${roomId}`, (msg: ChatMessageWithState) => {
        console.log('Received message room:', msg);
        if(msg.sender?.id !== this.userService.user()?.id) {
          this.addMessage({ ...msg, status: MessageStatus.RECEIVED });
        }
      });

      // Subscribe to user-specific queue (direct messages + confirmations)
      this.userSub = this.ws.subscribe(`/user/queue/private`, (msg: MessageSendConfirmation | ChatMessageWithState) => {
        // Delivery confirmation
        console.log('Received message:', msg);
        if ((msg as MessageSendConfirmation).sentMessageTempId) {
          const confirmation = msg as MessageSendConfirmation;
          this.updateMessageStatus(confirmation.sentMessageTempId, MessageStatus.DELIVERED);
        } else {
          // Direct message from another user
          const directMsg = msg as ChatMessageWithState;
          this.addMessage({ ...directMsg, status: MessageStatus.RECEIVED });
        }
      });
    });
  }

  /** Disconnect from WebSocket */
  disconnect() {
    this.roomSub?.unsubscribe();
    this.userSub?.unsubscribe();
  }

  /**
   * Send a message via WebSocket
   * @param payload MessageRequest DTO
   * @param currentUserId ID of current user (used for optimistic update)
   */
  sendMessage(payload: MessageRequest, currentUserId: string) {
    // Optimistic UI update
    const tempMessage: ChatMessageWithState = {
      ...payload,
      sender: { id: currentUserId } as Player,
      status: MessageStatus.SENDING,
      messageType: MessageType.USER,
      createdAt: new Date().toISOString(),
    };

    this.addMessage(tempMessage);

    // Send message to backend
    this.ws.publish({
      destination: '/app/room.send_message',
      body: JSON.stringify(payload),
    });
  }

  /** Add a message to reactive signal */
  private addMessage(msg: ChatMessageWithState) {
    this.messages.set([...this.messages(), msg]);
  }

  /** Update message status by tempId */
  private updateMessageStatus(tempId: string, status: MessageStatus) {
    this.messages.set(
      this.messages().map((m) => (m.tempId === tempId ? { ...m, status } : m))
    );
  }
}
