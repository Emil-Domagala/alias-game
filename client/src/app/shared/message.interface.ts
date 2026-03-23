import {Player} from './player.interface';

/** Conversation type (room, team, direct) */
export enum ConversationType {
  ROOM = 'ROOM',
  TEAM = 'TEAM',
  DIRECT = 'DIRECT',
};

/** Type of message */
export type MessageType = 'SYSTEM' | 'USER' | 'USER_GUESS' | 'USER_HINT';

/** Message DTO from the backend */
export interface ChatMessage {
  id: string;
  sender: Player | null;    // null for SYSTEM messages
  conversationId: string;
  content: string;
  conversationType: ConversationType;
  messageType: MessageType;
  createdAt: number;
}
