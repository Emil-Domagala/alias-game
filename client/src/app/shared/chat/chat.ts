import {Component, inject, input, output, signal} from '@angular/core';
import { ChatMessageWithState, ConversationType, MessageRequest} from '../message.interface';
import {UserService} from '../../user/user.service';
import {Player} from '../player.interface';
import {Room} from '../../room/room.interface';
import { v4 as uuidv4 } from 'uuid';
import {MessageComponent} from './message/message.component';

@Component({
  selector: 'app-chat',
  imports: [
    MessageComponent
  ],
  templateUrl: './chat.html',
  styleUrl: './chat.scss',
})
export class Chat {
  baseConversationType = input.required<Exclude<ConversationType, 'DIRECT'>>();
  messages = input.required<ChatMessageWithState[]>();
  currentRoom = input.required<Room>();

  currentTeam = input<{ id: string; players: Player[] }>();
  teams = input<{ id: string; players: Player[] }[]>([]);

  sendMessage = output<MessageRequest>();

  private userService = inject(UserService);
  currentUser = this.userService.user;

  newMessage = signal('');
  mentionSuggestions = signal<Player[]>([]);
  mentionedEntity = signal<Player | undefined>(undefined);

  onInputChange() {
    const value = this.newMessage();
    const atIndex = value.lastIndexOf('@');
    if (atIndex === -1) {
      this.mentionSuggestions.set([]);
      return;
    }

    const query = value.slice(atIndex + 1).toLowerCase();

    let suggestions: Player[] = [];
    const baseType = this.baseConversationType();
    const currentTeam = this.currentTeam?.();
    const teams = this.teams?.() ?? [];

    if (baseType === ConversationType.TEAM && currentTeam) {
      suggestions = currentTeam.players.filter(p => p.nick.toLowerCase().startsWith(query)
      );
    } else {
      suggestions = teams.flatMap(t => t.players)
        .filter(p => p.nick.toLowerCase().startsWith(query));
    }
    console.log('suggestions', suggestions);
    this.mentionSuggestions.set(suggestions);
  }

  selectMention(player: Player) {
    const value = this.newMessage();
    const atIndex = value.lastIndexOf('@');
    const before = value.slice(0, atIndex);
    const after = value.slice(atIndex + 1).replace(/^\w*/, ''); // remove typed text
    this.newMessage.set(`${before}@${player.nick} ${after}`.trim() + ' ');
    this.mentionSuggestions.set([]);
    this.mentionedEntity.set(player);
  }

  submitMessage() {
    const raw = this.newMessage().trim();
    if (!raw) return;

    const baseType = this.baseConversationType();
    const currentTeam = this.currentTeam?.();
    const currentRoom = this.currentRoom();
    const playerMention = this.mentionedEntity();

    let convType: ConversationType = baseType;
    let convId: string;

    const teamMention = raw.match(/^@Team\b/i);

    if (teamMention && currentTeam) {
      convType = ConversationType.TEAM;
      convId = currentTeam.id;
    } else if (playerMention) {
      convType = ConversationType.DIRECT;
      convId = playerMention.id;
    } else {
      convType = ConversationType.ROOM;
      convId = currentRoom.id;
    }

    this.sendMessage.emit({
      tempId: uuidv4(),
      content: raw,
      conversationType: convType,
      conversationId: convId,
      targetUserId: playerMention?.id ?? undefined,
    });

    this.newMessage.set('');
    this.mentionSuggestions.set([]);
    this.mentionedEntity.set(undefined);
  }
}
