import {inject, Injectable, signal} from '@angular/core';
import {API, ApiService} from '../api.service';
import {mapUserResponseToUser, User, UserResponse} from './user.interface';
import {firstValueFrom} from 'rxjs';
import {UserRole} from './user-role.enum';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly _user = signal<User | null>(null);
  private _loading = signal(false);

  private api = inject(ApiService);

  private fetchPromise: Promise<User | null> | null = null;

  /** Read-only signal for components */
  get user() {
    return this._user.asReadonly();
  }

  get isAuthenticated() {
    return !!this._user();
  }

  get isAdmin() {
    return this._user()?.roles.includes(UserRole.ADMIN);
  }

  /** Fetch user from backend only if not cached */
  async fetchUser(): Promise<User | null> {
    if (this._user()) {
      return this._user();
    }

    if (this.fetchPromise) {
      return this.fetchPromise;
    }

    // create new fetch promise
    this._loading.set(true);
    this.fetchPromise = firstValueFrom(
      this.api.get<UserResponse>(`${API.V1.PRIVATE}/user/me`)
    )
      .then((response) => {
        const user = mapUserResponseToUser(response.data);
        this._user.set(user);
        return user;
      })
      .catch((e) => {
        console.error('Failed to fetch user', e);
        this._user.set(null);
        return null;
      })
      .finally(() => {
        this._loading.set(false);
        this.fetchPromise = null;
      });

    return this.fetchPromise;
  }

  async refreshUser(): Promise<User | null> {
    this._user.set(null);
    return this.fetchUser();
  }
}
