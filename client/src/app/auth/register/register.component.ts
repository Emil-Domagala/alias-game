import { Component, inject, signal} from '@angular/core';
import {form, required, email, minLength, maxLength, FormField} from '@angular/forms/signals';
import {UserRegisterRequest} from './UserRegisterRequest.interface';
import {USER_CONSTRAINTS} from '../user.constraints';
import {CommonModule} from '@angular/common';
import {FormFieldComponent} from '../../shared/form/form-field/form-field.component';
import {RouterLink} from '@angular/router';
import {AuthService} from '../auth.service';
import {AUTH_ROUTES_FULL} from '../auth.router';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormFieldComponent, RouterLink, FormField],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  authService = inject(AuthService);
  isSubmitting = signal(false);

  registerModel = signal<UserRegisterRequest>({
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

  async submit(event: Event) {
    event.preventDefault();

    if (this.registerForm().invalid()) return;

    this.isSubmitting.set(true);

    try {
      const user = await this.authService.register(this.registerForm().value());
    } catch (err) {
      console.error('Registration error', err);
    } finally {
      this.isSubmitting.set(false);
    }
  }

  protected readonly AUTH_ROUTES_FULL = AUTH_ROUTES_FULL;
}
