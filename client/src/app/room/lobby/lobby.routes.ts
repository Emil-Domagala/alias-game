import {Routes} from '@angular/router';
import {LobbyComponent} from './lobby.component';

export const LOBBY_ROUTES_SEGMENTS = {
  ROOT: 'lobby',
} as const;
export const LOBBY_ROUTES_FULL = {
  LOBBY: `/${LOBBY_ROUTES_SEGMENTS.ROOT}`
} as const;

export const LOBBY_ROUTES: Routes = [
  {path:'', component: LobbyComponent }
];
