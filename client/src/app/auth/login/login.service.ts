import {inject, Injectable, signal} from '@angular/core';
import {API, ApiService} from '../../api.service';
import {UserRegisterRequestInterface} from '../register/UserRegisterRequest.interface';
import {finalize} from 'rxjs';
import {UserLoginRequest} from './UserLoginRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  api = inject(ApiService);
  isLoading = signal(false);

  register(data: UserLoginRequest) {
    this.isLoading.set(true);

    return this.api
      .post<void>(`${API.V1.PUBLIC}/auth/login`, data)
      .pipe(
        finalize(() => this.isLoading.set(false))
      );
  }
}
