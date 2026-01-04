import { Injectable, inject, signal } from '@angular/core';
import { finalize } from 'rxjs';
import { ApiService, API } from '../api.service';

import {UserRegisterRequest} from './register/UserRegisterRequest.interface';
import {UserLoginRequest} from './login/UserLoginRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private api = inject(ApiService);

  readonly isLoading = signal(false);

  login(data: UserLoginRequest) {
    this.isLoading.set(true);

    return this.api
      .post<void>(`${API.V1.PUBLIC}/auth/login`, data)
      .pipe(
        finalize(() => this.isLoading.set(false))
      );
  }

  register(data: UserRegisterRequest) {
    this.isLoading.set(true);

    return this.api
      .post<void>(`${API.V1.PUBLIC}/auth/register`, data)
      .pipe(
        finalize(() => this.isLoading.set(false))
      );
  }
}
