import { Routes } from '@angular/router';
import {RegisterComponent} from './auth/register/register.component';
import {LoginComponent} from './auth/login/login.component';
import {RoomComponent} from './room/room/room.component';
import {LobbyComponent} from './room/lobby/lobby.component';
import {AUTH_ROUTES, AUTH_ROUTES_SEGMENTS} from './auth/auth.routes';
import {authResolver} from './auth/auth.resolver';
import {ROOM_ROUTES, ROOM_ROUTES_SEGMENTS} from './room/room/room.routes';
import {LOBBY_ROUTES, LOBBY_ROUTES_SEGMENTS} from './room/lobby/lobby.routes';

export const routes: Routes = [
  {path: AUTH_ROUTES_SEGMENTS.ROOT, children: AUTH_ROUTES },
  { path: LOBBY_ROUTES_SEGMENTS.ROOT, children: LOBBY_ROUTES, resolve: { auth: authResolver} },
  { path: ROOM_ROUTES_SEGMENTS.ROOT, children: ROOM_ROUTES, resolve: { auth: authResolver} },
];
