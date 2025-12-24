import {inject, Injectable, signal} from '@angular/core';
import {API, ApiService} from '../../api.service';
import {UserRegisterRequestInterface} from './UserRegisterRequest.interface';
import {finalize} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  api = inject(ApiService);
  isLoading = signal(false);

  register(data: UserRegisterRequestInterface) {
    console.log("trying to log user")
    this.isLoading.set(true);

    return this.api
      .post<void>(`${API.V1.PUBLIC}/auth/register`, data)
      .pipe(
        finalize(() => this.isLoading.set(false))
      );
  }
}

