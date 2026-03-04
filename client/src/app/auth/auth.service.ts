import { Injectable, inject, signal } from '@angular/core';
import {finalize, firstValueFrom} from 'rxjs';
import { ApiService, API } from '../api.service';

import {UserRegisterRequest} from './register/UserRegisterRequest.interface';
import {UserLoginRequest} from './login/UserLoginRequest.interface';
import {mapUserResponseToUser, User, UserResponse} from '../user/user.interface';
import {UserService} from '../user/user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private api = inject(ApiService);
  private userService = inject(UserService);


  /** Login and directly update UserService */
  async login(data: UserLoginRequest): Promise<User | null> {
    try {
      const res = await firstValueFrom(
        this.api.post<UserResponse>(`${API.V1.PUBLIC}/auth/login`, data)
      );
      const user = mapUserResponseToUser(res.data);
      this.userService.setUser(user);
      return user;
    } catch (e) {
      console.error('Login failed', e);
      this.userService.clearUser();
      return null;
    }
  }

  /** Register and directly update UserService */
  async register(data: UserRegisterRequest): Promise<User | null> {
    try {
      const res = await firstValueFrom(
        this.api.post<UserResponse>(`${API.V1.PUBLIC}/auth/register`, data)
      );
      const user = mapUserResponseToUser(res.data);
      this.userService.setUser(user);
      return user;
    } catch (e) {
      console.error('Register failed', e);
      this.userService.clearUser();
      return null;
    }
  }

  /** Logout user */
  async logout(): Promise<void> {
    try {
      await firstValueFrom(this.api.post<void>(`${API.V1.PUBLIC}/auth/logout`, {}));
    } catch (e) {
      console.error('Logout failed', e);
    } finally {
      this.userService.clearUser();
    }
  }
}
