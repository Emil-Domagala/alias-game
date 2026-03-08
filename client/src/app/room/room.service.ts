import { Injectable, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ApiService, API } from '../api.service';
import { PaginationResult } from '../pagination-result.interface';
import {RoomDto} from './roomDto.interface';
import {HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  private api = inject(ApiService);

  private _currentRoom = signal<RoomDto | null>(null);
  currentRoom = this._currentRoom.asReadonly();

  private _rooms = signal<RoomDto[]>([]);
  rooms = this._rooms.asReadonly();

  async createRoom(data: any): Promise<RoomDto | null> {
    try {
      const res = await firstValueFrom(this.api.post<RoomDto>(`${API.V1.PRIVATE}/room/create`, data));
      this._currentRoom.set(res.data);
      return res.data;
    } catch (e) {
      console.error('Failed to create room', e);
      return null;
    }
  }

  async getRooms(params?: { page?: number; size?: number; sortField?: string; direction?: 'ASC' | 'DESC' }): Promise<PaginationResult<RoomDto> | null> {
    try {
      let httpParams = new HttpParams();
      if (params?.page != null) httpParams = httpParams.set('page', params.page);
      if (params?.size != null) httpParams = httpParams.set('size', params.size);
      if (params?.sortField != null) httpParams = httpParams.set('sortField', params.sortField);
      if (params?.direction != null) httpParams = httpParams.set('direction', params.direction);

      const res = await firstValueFrom(this.api.get<PaginationResult<RoomDto>>(`${API.V1.PRIVATE}/room`, httpParams));
      this._rooms.set(res.data.content);
      return res.data;
    } catch (e) {
      console.error('Failed to fetch rooms', e);
      return null;
    }
  }

  async joinRoom(roomId: string): Promise<RoomDto | null> {
    try {
      const res = await firstValueFrom(this.api.post<RoomDto>(`${API.V1.PRIVATE}/room/${roomId}/join`, {}));
      this._currentRoom.set(res.data);
      return res.data;
    } catch (e) {
      console.error('Failed to join room', e);
      return null;
    }
  }

  async leaveRoom(roomId: string): Promise<void> {
    try {
      await firstValueFrom(this.api.post<void>(`${API.V1.PRIVATE}/room/${roomId}/leave`, {}));
      this._currentRoom.set(null);
    } catch (e) {
      console.error('Failed to leave room', e);
    }
  }

  async getCurrentRoom(): Promise<RoomDto | null> {
    try {
      const res = await firstValueFrom(
        this.api.get<RoomDto>(`${API.V1.PRIVATE}/room/current`)
      );
      this._currentRoom.set(res.data ?? null);
      return res.data ?? null;
    } catch (e) {
      console.warn('User is not in any room', e);
      this._currentRoom.set(null);
      return null;
    }
  }

  setCurrentRoom(room: RoomDto) {
    this._currentRoom.set(room);
  }
}
