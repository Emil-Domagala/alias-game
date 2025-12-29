import {inject, Injectable, signal} from '@angular/core';
import {API, ApiService} from '../api.service';
import {finalize} from 'rxjs';

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
}
