import { Routes } from '@angular/router';
import {RegisterComponent} from './auth/register/register.component';
import {LoginComponent} from './auth/login/login.component';
import {RoomComponent} from './room/room.component';
import {LobbyComponent} from './lobby/lobby.component';

export const routes: Routes = [
  { path: 'lobby', component: LobbyComponent },
  { path: 'room/:id', component: RoomComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
];
