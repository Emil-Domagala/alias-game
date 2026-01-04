import {inject, Injectable, signal} from '@angular/core';
import {API, ApiService} from '../api.service';
import {finalize} from 'rxjs';
import {Room} from './room.interface';
import {PaginationResult} from '../pagination-result.interface';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  api = inject(ApiService);
  isLoading = signal(false);

  createRoom(data:any){
    this.isLoading.set(true);

    return this.api
      .post<void>(`${API.V1.PRIVATE}/room/create`, data)
      .pipe(
        finalize(() => this.isLoading.set(false))
      );
  }

  getRooms(params?: {
    page?: number;
    size?: number;
    sortField?: string;
    direction?: 'ASC' | 'DESC';
  }) {
    this.isLoading.set(true);

    return this.api.get<PaginationResult<Room>>(
      `${API.V1.PRIVATE}/room`
    ).pipe(
      finalize(() => this.isLoading.set(false))
    );
  }


  joinRoom(roomId: string) {
    this.isLoading.set(true);

    return this.api.post<Room>(
      `${API.V1.PRIVATE}/room/${roomId}/join`,
      {}
    ).pipe(
      finalize(() => this.isLoading.set(false))
    );
  }

  leaveRoom(roomId: string) {
    this.isLoading.set(true);

    return this.api.post<Room>(
      `${API.V1.PRIVATE}/room/${roomId}/leave`,
      {}
    ).pipe(
      finalize(() => this.isLoading.set(false))
    );
  }

  deleteRoom(roomId: string) {
    this.isLoading.set(true);

    return this.api.delete<void>(
      `${API.V1.PRIVATE}/room/delete/${roomId}`
    ).pipe(
      finalize(() => this.isLoading.set(false))
    );
  }
}
