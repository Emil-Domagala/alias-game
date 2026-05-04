import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import { Chat } from '../../shared/chat/chat';
import {ConversationType, MessageRequest} from '../../shared/message.interface';
import { UserService } from '../../user/user.service';
import { RoomMessageService } from './room-message.service';
import { RoomService } from '../room.service';
import { Room } from '../room.interface';
import {SkeletonDirective} from '../../shared/skeleton/skeleton.directive';

@Component({
  selector: 'app-room',
  standalone: true,
  templateUrl: './room.component.html',
  imports: [Chat, SkeletonDirective],
  styleUrls: ['./room.component.scss'],
})
export class RoomComponent implements OnInit, OnDestroy {
  private roomService = inject(RoomService);
  private currentUser = inject(UserService).user;
  private messageService = inject(RoomMessageService);

  loading = signal(true);
  room: Room | undefined;
  roomId!: string;
  messages = this.messageService.messages;

  ngOnInit() {
   void this.loadRoom();
  }

  protected async loadRoom(){
    this.loading.set(true);

    this.room = (await this.roomService.getCurrentRoom())!;

    if (!this.room) {
      throw new Error('User is not in a room');
    }

    this.roomId = this.room.id;

    this.messageService.connect(this.roomId, this.currentUser()?.id!);
    this.loading.set(false);
  }

  ngOnDestroy() {
    this.messageService.disconnect();
  }

  protected handleSendMessage(message: MessageRequest) {
    console.log('Sending message:', message);
    this.messageService.sendMessage(message, this.currentUser()?.id!);
  }

  protected readonly ConversationType = ConversationType;
}
