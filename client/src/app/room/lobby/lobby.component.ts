import {Component, effect, inject, OnDestroy, OnInit, runInInjectionContext, signal} from '@angular/core';
import { StompSubscription } from '@stomp/stompjs';
import { CreateRoomModalComponent } from '../create-room-modal/create-room-modal.component';
import { RoomService } from '../room.service';
import { ModalService } from '../../shared/modal/modal.service';
import { RoomsListComponent } from './rooms-list/rooms-list.component';
import { RoomDto } from '../roomDto.interface';
import { WebSocketService } from '../../web-socket.service';
import {QueryConfig} from '../../shared/query/query-config-model.model';
import {Router} from '@angular/router';
import {QueryLayoutComponent} from '../../shared/query/query-layout.component/query-layout.component';
import {QueryStateService} from '../../shared/query/query-state.service';

@Component({
  selector: 'app-lobby',
  imports: [RoomsListComponent, QueryLayoutComponent],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.scss',
})
export class LobbyComponent implements OnInit, OnDestroy {
  private modal = inject(ModalService);
  private service = inject(RoomService);
  private ws = inject(WebSocketService);
  private router = inject(Router);
  private queryState = inject(QueryStateService);

  private wsSub?: StompSubscription;

  rooms = signal<RoomDto[]>([]);
  config = signal<QueryConfig | null>(null);

  ngOnInit() {
    this.loadConfig();
    this.loadRooms();
    this.connectWebSocket();
    this.loadCurrentRoom();
  }

  loadRoomsEffect = effect(() => {
    console.log('Query state changed:', this.queryState);
    const search = this.queryState.search();
    const filters = this.queryState.filters();
    const sortField = this.queryState.sortField();
    const direction = this.queryState.direction();
    const pageSize = this.queryState.pageSize();

    this.loadRooms();
  });

  private async loadConfig() {
    const config = await this.service.getRoomConfig();
    this.config.set(config);
    console.log('Room service config:', config);
  }

  /** Load rooms from backend */
  private async loadRooms() {
    const state = this.queryState;
    const result = await this.service.getRooms({
      page: 0, //TODO: add pagination
      size: state.pageSize(),
      sortField: state.sortField(),
      direction: state.direction(),
      filters: state.filters(),
      search: state.search(),
    });

    if (result?.content) {
      runInInjectionContext(this.constructor as any, () => {
        this.rooms.set(result.content);
      });
    }
  }

  /** Fetch the current room for the user */
  private async loadCurrentRoom() {
    const currentRoom = await this.service.getCurrentRoom();
    if (currentRoom) {
      console.log('User is already in a room:', currentRoom);
      this.router.navigate(['/room', currentRoom.id]);
    }
  }

  /** Connect to lobby websocket */
  private connectWebSocket() {
    this.ws.connect(() => {
      this.wsSub = this.ws.subscribe('/topic/lobby', (msg) => {
        console.log('Lobby event:', msg);
        // Optional: refresh rooms on lobby events
      });
    });
  }

  /** Open create room modal */
  openCreateRoomModal() {
    const ref = this.modal.open(CreateRoomModalComponent);
    ref.closed.subscribe(async (result) => {
      if (result) {
        console.log('Room created:', result);
        // Refresh lobby rooms after creation
      }
    });
  }

  ngOnDestroy() {
    this.wsSub?.unsubscribe();
    this.ws.disconnect();
  }
}
