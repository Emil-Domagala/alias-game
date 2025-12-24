import {ChangeDetectionStrategy, Component, inject, signal} from '@angular/core';
import {form, Field, required, email, minLength, maxLength} from '@angular/forms/signals';
import {UserRegisterRequestInterface} from './UserRegisterRequest.interface';
import {USER_CONSTRAINTS} from '../user.constraints';
import {RegisterService} from './register.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [Field, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  registerService = inject(RegisterService);

  registerModel = signal<UserRegisterRequestInterface>({
    email: '',
    nick: '',
    password: '',
  })

  registerForm = form(this.registerModel,(shemaPath)=>{
    required(shemaPath.email,{message: "Email is required"});
    required(shemaPath.nick,{message: "Nick is required"});
    required(shemaPath.password,{message: "Password is required"});
    email(shemaPath.email,{message: "Email is invalid"});
    minLength(shemaPath.nick,USER_CONSTRAINTS.NICK_MIN,{ message: "Nick is too short"});
    minLength(shemaPath.password,USER_CONSTRAINTS.PASSWORD_MIN,{ message: "Password is too short"});
    maxLength(shemaPath.password,USER_CONSTRAINTS.PASSWORD_MAX,{ message: "Password is too long"});
    maxLength(shemaPath.nick,USER_CONSTRAINTS.NICK_MAX,{ message: "Nick is too long"});
  })

  submit(event: Event){
    event.preventDefault();
    if (this.registerForm().invalid()) return;

    this.registerService
      .register(this.registerForm().value())
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
