import {Routes} from '@angular/router';
import {AuthCardComponent} from './auth-card.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

export const AUTH_ROUTES: Routes=[
  {path:'', component: AuthCardComponent, children:[
      {path:'login',component:LoginComponent},
      {path:'register',component:RegisterComponent},
      {path:'',pathMatch:'full',redirectTo:'login'}
    ]}
]
