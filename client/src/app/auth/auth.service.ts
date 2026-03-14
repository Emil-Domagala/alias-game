import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ApiService, API } from '../api.service';
import { UserRegisterRequest } from './register/UserRegisterRequest.interface';
import { UserLoginRequest } from './login/UserLoginRequest.interface';
import { mapUserResponseToUser, User, UserResponse } from '../user/user.interface';
import { UserService } from '../user/user.service';
import { mapApiError, ApiError } from '../api-error.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private api = inject(ApiService);
  private userService = inject(UserService);

  /**
   * Logs in a user using provided credentials and updates the UserService.
   *
   * @param data - The user login request payload
   * @returns A Promise resolving to the logged-in User
   * @throws {ApiError}
   */
  async login(data: UserLoginRequest): Promise<User | null> {
    try {
      const res = await firstValueFrom(
        this.api.post<UserResponse>(`${API.V1.PUBLIC}/auth/login`, data)
      );
      const user = mapUserResponseToUser(res.data);
      this.userService.setUser(user);
      return user;
    } catch (e) {
      this.userService.clearUser();
      throw mapApiError(e);
    }
  }

  /**
   * Registers a new user and updates the UserService.
   *
   * @param data - The user registration request payload
   * @returns A Promise resolving to the newly registered User
   * @throws {ApiError}
   */
  async register(data: UserRegisterRequest): Promise<User | null> {
    try {
      const res = await firstValueFrom(
        this.api.post<UserResponse>(`${API.V1.PUBLIC}/auth/register`, data)
      );
      const user = mapUserResponseToUser(res.data);
      this.userService.setUser(user);
      return user;
    } catch (e) {
      this.userService.clearUser();
      throw mapApiError(e);
    }
  }

  /**
   * Logs out the current user and clears the UserService.
   *
   * @returns A Promise that resolves when the logout is complete
   * @throws {ApiError}
   */
  async logout(): Promise<void> {
    try {
      await firstValueFrom(this.api.post<void>(`${API.V1.PUBLIC}/auth/logout`, {}));
    } catch (e) {
      throw mapApiError(e);
    } finally {
      this.userService.clearUser();
    }
  }
}
