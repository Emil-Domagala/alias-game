import { Component, inject, signal} from '@angular/core';
import {form, required, email, minLength, maxLength, FormField, submit} from '@angular/forms/signals';
import {UserRegisterRequest} from './UserRegisterRequest.interface';
import {USER_CONSTRAINTS} from '../user.constraints';
import {CommonModule} from '@angular/common';
import {FormFieldComponent} from '../../shared/form/form-field/form-field.component';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../auth.service';
import {AUTH_ROUTES_FULL} from '../auth.routes';
import {LOBBY_ROUTES_FULL} from '../../room/lobby/lobby.routes';
import {ToastrService} from 'ngx-toastr';
import {ApiErrorMapperService} from '../../shared/form/api-error-mapper.service';
import {ApiError} from '../../api-error.model';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormFieldComponent, RouterLink, FormField],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  private router = inject(Router);
  private authService = inject(AuthService);
  private toastrService = inject(ToastrService);
  private apiErrorMapperService = inject(ApiErrorMapperService);

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

  async onSubmit(event: Event) {
    event.preventDefault();

    event.preventDefault();

    await submit(this.registerForm, async (form) => {
      try {
        const user = await this.authService.register(form().value());

        if (user) {
          this.toastrService.success('Account created!');
          await this.router.navigate([LOBBY_ROUTES_FULL.LOBBY]);
        }

        return;
      } catch (err) {
        const apiError = err as ApiError;

        this.toastrService.error(apiError.message, 'Registration failed');

        return this.apiErrorMapperService.mapApiErrorToValidationErrors(apiError, form);
      }
    });
  }

  protected readonly AUTH_ROUTES_FULL = AUTH_ROUTES_FULL;
}
