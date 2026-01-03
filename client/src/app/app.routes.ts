import { Routes } from '@angular/router';
import {RegisterComponent} from './auth/register/register.component';
import {LoginComponent} from './auth/login/login.component';
import {RoomComponent} from './room/room.component';
import {LobbyComponent} from './lobby/lobby.component';
import {AUTH_ROUTES} from './auth/auth.router';

export const routes: Routes = [
  {path: 'auth', children: AUTH_ROUTES },
  { path: 'lobby', component: LobbyComponent },
  { path: 'room/:id', component: RoomComponent },
];
