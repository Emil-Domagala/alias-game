import {Routes} from '@angular/router';
import {AuthCardComponent} from './auth-card.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

export const AUTH_ROUTES_SEGMENTS = {
  ROOT: 'auth',
  LOGIN: 'login',
  REGISTER: 'register',
} as const;

export const AUTH_ROUTES_FULL = {
  LOGIN: [ '/', AUTH_ROUTES_SEGMENTS.ROOT, AUTH_ROUTES_SEGMENTS.LOGIN],
  REGISTER: [ '/', AUTH_ROUTES_SEGMENTS.ROOT, AUTH_ROUTES_SEGMENTS.REGISTER],
} as const;


export const AUTH_ROUTES: Routes=[
  {path:'', component: AuthCardComponent, children:[
      {path: AUTH_ROUTES_SEGMENTS.LOGIN, component:LoginComponent},
      {path: AUTH_ROUTES_SEGMENTS.REGISTER, component:RegisterComponent},
      {path:'',pathMatch:'full',redirectTo: AUTH_ROUTES_SEGMENTS.LOGIN}
    ]}
]
