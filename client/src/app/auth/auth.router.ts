import {Routes} from '@angular/router';
import {AuthCard} from './auth-card';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

export const AUTH_ROUTES: Routes=[
  {path:'', component: AuthCard, children:[
      {path:'login',component:LoginComponent},
      {path:'register',component:RegisterComponent},
      {path:'',pathMatch:'full',redirectTo:'login'}
    ]}
]
