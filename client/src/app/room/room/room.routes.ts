import {Routes} from '@angular/router';
import {RoomComponent} from './room.component';

export const ROOM_ROUTES_SEGMENTS = {
  ROOT: 'room',
  ID: ':id'
} as const;
export const ROOM_ROUTES_FULL = {
  ROOM: `/${ROOM_ROUTES_SEGMENTS.ROOT}/${ROOM_ROUTES_SEGMENTS.ID}`
} as const;

export const ROOM_ROUTES: Routes = [
  {path:ROOM_ROUTES_SEGMENTS.ID, component: RoomComponent }
];
