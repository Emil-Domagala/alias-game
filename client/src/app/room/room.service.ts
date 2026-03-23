import {Injectable, inject, signal, WritableSignal, Signal} from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ApiService, API } from '../api.service';
import { PaginationResult } from '../pagination-result.interface';
import {HttpParams} from '@angular/common/http';
import {QueryConfig, QueryFilter} from '../shared/query/query-config-model.model';
import {mapApiError} from '../api-error.model';
import {Room} from './room.interface';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  private api = inject(ApiService);

  private _currentRoom = signal<Room | null>(null);
  currentRoom: Signal<Room | null> = this._currentRoom.asReadonly();

  private _rooms = signal<Room[]>([]);
  rooms = this._rooms.asReadonly();

  /**
   * Fetches query configuration for rooms.
   *
   * The configuration describes which fields are filterable,
   * searchable, and sortable when querying rooms.
   *
   * @returns A {@link QueryConfig} describing available filters and sorting options.
   *
   * @throws {ApiError}
   */
  async getRoomConfig(): Promise<QueryConfig | null> {
    try {
      const res = await firstValueFrom(
        this.api.get<QueryConfig>(`${API.V1.PRIVATE}/room/config`)
      );

      return res.data;
    } catch (e) {
      throw mapApiError(e);
    }
  }

  /**
   * Creates a new room.
   *
   * On success, the created room becomes the current room.
   *
   * @param data Room creation payload.
   *
   * @returns The created {@link Room}.
   *
   * @throws {ApiError}
   */
  async createRoom(data: any): Promise<Room | null> {
    try {
      const res = await firstValueFrom(this.api.post<Room>(`${API.V1.PRIVATE}/room/create`, data));
      this._currentRoom.set(res.data);
      return res.data;
    } catch (e) {
      throw mapApiError(e);
    }
  }

  /**
   * Fetches a paginated list of rooms.
   *
   * Supports pagination, sorting, filtering, and full-text search.
   *
   * The internal `rooms` signal is updated with the returned room list.
   *
   * @param params Optional query parameters.
   * @param params.page Page index (0-based).
   * @param params.size Page size.
   * @param params.sortField Field used for sorting.
   * @param params.direction Sort direction (`ASC` or `DESC`).
   * @param params.filters Optional query filters.
   * @param params.search Optional search string.
   *
   * @returns A {@link PaginationResult} containing rooms and metadata.
   *
   * @throws {ApiError}
   */
  async getRooms(params?: { page?: number; size?: number; sortField?: string; direction?: 'ASC' | 'DESC', filters?: QueryFilter[], search?: string }): Promise<PaginationResult<Room> | null> {
    try {
      let httpParams = new HttpParams();
      if (params?.page != null) httpParams = httpParams.set('page', params.page);
      if (params?.size != null) httpParams = httpParams.set('size', params.size);
      if (params?.sortField) httpParams = httpParams.set('sort', `${params.sortField},${params.direction ?? 'ASC'}`)
      if (params?.filters?.length) {
        params.filters.forEach(f => {
          httpParams = httpParams.append('filters', `${f.field}:${f.operator}:${f.value}`)
        })
      }
      if (params?.search) httpParams = httpParams.set('search', params.search);

      const res = await firstValueFrom(this.api.get<PaginationResult<Room>>(`${API.V1.PRIVATE}/room`, httpParams));
      this._rooms.set(res.data.content);
      return res.data;
    } catch (e) {
      throw mapApiError(e);
    }
  }

  /**
   * Joins an existing room.
   *
   * On success, the joined room becomes the current room.
   *
   * @param roomId Identifier of the room to join.
   *
   * @returns The joined {@link Room}.
   *
   * @throws {ApiError}
   */
  async joinRoom(roomId: string): Promise<Room | null> {
    try {
      const res = await firstValueFrom(this.api.post<Room>(`${API.V1.PRIVATE}/room/${roomId}/join`, {}));
      this._currentRoom.set(res.data);
      return res.data;
    } catch (e) {
      throw mapApiError(e);
    }
  }

  /**
   * Leaves the specified room.
   *
   * After a successful request, the current room signal is cleared.
   *
   * @param roomId Identifier of the room to leave.
   *
   * @throws {ApiError}
   */
  async leaveRoom(roomId: string): Promise<void> {
    try {
      await firstValueFrom(this.api.post<void>(`${API.V1.PRIVATE}/room/${roomId}/leave`, {}));
      this._currentRoom.set(null);
    } catch (e) {
      throw mapApiError(e);
    }
  }

  /**
   * Retrieves the current room from the backend if it is not already cached.
   *
   * If a room is already present in the local signal, the cached value is returned.
   *
   * @returns The current {@link Room}, or `null` if the user is not in a room.
   *
   * @throws {ApiError}
   */
  async getCurrentRoom(): Promise<Room | null> {
    if (this._currentRoom()) return this._currentRoom();
    try {
      const res = await firstValueFrom(
        this.api.get<Room>(`${API.V1.PRIVATE}/room/current`)
      );
      this._currentRoom.set(res.data ?? null);
      return res.data ?? null;
    } catch (e) {
      this._currentRoom.set(null);
      throw mapApiError(e);
    }
  }

  setCurrentRoom(room: Room) {
    this._currentRoom.set(room);
  }
}
