import {inject, Injectable, signal} from '@angular/core';
import {API, ApiService} from '../api.service';
import {mapUserResponseToUser, User, UserResponse} from './user.interface';
import {firstValueFrom} from 'rxjs';
import {UserRole} from './user-role.enum';
import {mapApiError} from '../api-error.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly _user = signal<User | null>(null);
  private _loading = signal(false);

  private api = inject(ApiService);

  /**
   * Cached in-flight fetch request.
   *
   * Ensures that multiple simultaneous calls to {@link fetchUser}
   * share the same network request instead of triggering duplicates.
   */
  private fetchPromise: Promise<User | null> | null = null;

  /** Read-only signal for components */
  get user() {
    return this._user.asReadonly();
  }

  setUser(user: User) {
    console.log('Setting user:', user);
    this._user.set(user);
  }

  /** Clear cached user */
  clearUser() {
    this._user.set(null);
  }

  /**
   * Indicates whether a user is currently authenticated.
   *
   * @returns `true` if a user exists in the local state.
   */
  get isAuthenticated() {
    return !!this._user();
  }

  /**
   * Indicates whether the current user has the `ADMIN` role.
   *
   * @returns `true` if the authenticated user has administrator privileges.
   */
  get isAdmin() {
    return this._user()?.roles.includes(UserRole.ADMIN);
  }

  /**
   * Fetches the currently authenticated user from the backend.
   *
   * This method uses an internal cache and concurrency guard:
   *
   * - If a user is already loaded locally, the cached value is returned.
   * - If a fetch request is already in progress, the same promise is reused.
   * - Otherwise, a new request to `/user/me` is issued.
   *
   * @returns A promise resolving to the authenticated {@link User}, or `null`
   *          if no user is authenticated.
   *
   * @throws {ApiError}
   */
  async fetchUser(): Promise<User | null> {
    console.log('Fetching user');
    if (this._user()) {
      return this._user();
    }

    if (this.fetchPromise) {
      return this.fetchPromise;
    }

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
        this._user.set(null);
        throw mapApiError(e);
      })
      .finally(() => {
        this._loading.set(false);
        this.fetchPromise = null;
      });

    return this.fetchPromise;
  }

  /**
   * Forces a refresh of the authenticated user from the backend.
   *
   * This clears the cached user and performs a new fetch request.
   *
   * @returns A promise resolving to the refreshed {@link User}, or `null`
   *          if no user is authenticated.
   *
   * @throws {ApiError}
   */
  async refreshUser(): Promise<User | null> {
    this._user.set(null);
    return this.fetchUser();
  }
}
