import { Player } from './player.interface';

export enum ConversationType {
  ROOM = 'ROOM',
  TEAM = 'TEAM',
  DIRECT = 'DIRECT',
}

export enum MessageType {
  SYSTEM = 'SYSTEM',
  USER = 'USER',
  USER_GUESS = 'USER_GUESS',
  USER_HINT = 'USER_HINT',
}

/** Message DTO from the backend */
export interface ChatMessage {
  id?: string;                   // UUID from backend
  tempId?: string;        // tempId for optimistic updates
  sender: Player | null;        // null for SYSTEM messages
  conversationId: string;
  targetUserId?: string | null; // optional target user for direct messages
  content: string;
  conversationType: ConversationType;
  messageType: MessageType;
  createdAt: string;
}

export enum MessageStatus {
  SENDING = 'SENDING',
  DELIVERED = 'DELIVERED',
  RECEIVED = 'RECEIVED',
}

/** Message with local UI state (sending/delivered/received) */
export interface ChatMessageWithState extends ChatMessage {
  status: MessageStatus;
}

/** Confirmation DTO from backend */
export interface MessageSendConfirmation {
  messageId: string;
  sentMessageTempId: string;
  conversationType: ConversationType;
  conversationId: string;
}

export interface MessageRequest {
  tempId: string;
  conversationId: string;
  conversationType: ConversationType;
  content: string;
  targetUserId?: string;
}
