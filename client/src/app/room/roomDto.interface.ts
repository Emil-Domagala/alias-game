import {RoomStatus} from './room-status.enum';
import {Player} from '../shared/player.interface';


export interface RoomDto {
  id: string;
  name: string;
  owner: Player;
  maxPlayers: number;
  minPlayers: number;
  playersCount: number;
  roomStatus: RoomStatus;
  numberOfTeams: number;
}

/** Allowed sorting fields (matches backend) */
export const ROOM_ALLOWED_SORT_FIELDS = new Set([
  'id',
  'name',
  'ownerId',
  'maxPlayers',
  'minPlayers',
  'playersCount',
]);

export const ROOM_DEFAULT_SORT_FIELD = 'playersCount';
