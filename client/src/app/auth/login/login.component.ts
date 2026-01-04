import {Component, inject, signal} from '@angular/core';
import {UserLoginRequest} from './UserLoginRequest.interface';
import {email, Field, form, maxLength, minLength, required} from '@angular/forms/signals';
import {USER_CONSTRAINTS} from '../user.constraints';
import {FormFieldComponent} from '../../shared/form/form-field/form-field.component';
import {RouterLink} from '@angular/router';
import {AuthService} from '../auth.service';

@Component({
  selector: 'app-login',
  imports: [Field, FormFieldComponent, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authService = inject(AuthService);

  loginModel= signal<UserLoginRequest>({
    email:'',
    password:''
  })

  loginForm = form(this.loginModel,(shemaPath)=>{
    required(shemaPath.email,{message: "Email is required"});
    required(shemaPath.password,{message: "Password is required"});
    email(shemaPath.email,{message: "Email is invalid"});
    minLength(shemaPath.password,USER_CONSTRAINTS.PASSWORD_MIN,{ message: "Password is too short"});
    maxLength(shemaPath.password,USER_CONSTRAINTS.PASSWORD_MAX,{ message: "Password is too long"});
  })

  submit(event: Event){
    event.preventDefault();
    if (this.loginForm().invalid()) return;

    this.authService
      .login(this.loginForm().value())
      .subscribe({
        next: (data) => {
          console.log(data)
        },
        error: err => {
          // handle error
        },
      });
  }

}
