import {Component, inject, signal} from '@angular/core';
import {UserLoginRequest} from './UserLoginRequest.interface';
import {
  email,
  FieldTree,
  form,
  FormField,
  maxLength,
  minLength,
  required,
  submit,
  ValidationError
} from '@angular/forms/signals';
import {USER_CONSTRAINTS} from '../user.constraints';
import {FormFieldComponent} from '../../shared/form/form-field/form-field.component';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../auth.service';
import {AUTH_ROUTES_FULL} from '../auth.routes';
import {LOBBY_ROUTES_FULL} from '../../room/lobby/lobby.routes';
import { ToastrService } from 'ngx-toastr';
import {ApiError} from '../../api-error.model';
import {ApiErrorMapperService} from '../../shared/form/api-error-mapper.service';

@Component({
  selector: 'app-login',
  imports: [FormFieldComponent, RouterLink, FormField],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private router = inject(Router);
  private authService = inject(AuthService);
  private toastrService = inject(ToastrService);
  private apiErrorMapperService = inject(ApiErrorMapperService);

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

  async onSubmit(event: Event) {
    event.preventDefault();

    await submit(this.loginForm, async (form) => {
      try {
        const user = await this.authService.login(form().value());

        if (user) {
          this.toastrService.success('Welcome back!');
          await this.router.navigate([LOBBY_ROUTES_FULL.LOBBY]);
        }

        return undefined;
      } catch (err) {
        const apiError = err as ApiError;
        this.toastrService.error(apiError.message, 'Login failed');
        return this.apiErrorMapperService.mapApiErrorToValidationErrors(apiError, this.loginForm);
    }});
  }

  protected readonly AUTH_ROUTES_FULL = AUTH_ROUTES_FULL;
}
