import {Player} from '../shared/player.interface';
import {RoomStatus} from './room-status.enum';

export interface Room {
  id: string;
  name: string;
  owner: Player;
  maxPlayers: number;
  minPlayers: number;
  playersCount: number;
  roomStatus: RoomStatus;
  numberOfTeams: number;
}
