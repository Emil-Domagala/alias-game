import {RoomStatus} from './room-status.enum';
import {Player} from '../shared/player.interface';

export interface RoomWithPlayers {
  id: string;
  name: string;
  owner: Player;
  maxPlayers: number;
  minPlayers: number;
  players: Player[];
  status: RoomStatus;
  numberOfTeams: number;
}
