import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { StompSubscription } from '@stomp/stompjs';
import { CreateRoomModalComponent } from '../room/create-room-modal/create-room-modal.component';
import { RoomService } from '../room/room.service';
import { ModalService } from '../shared/modal/modal.service';
import { RoomsListComponent } from './rooms-list/rooms-list.component';
import { RoomDto } from '../room/roomDto.interface';
import { WebSocketService } from '../web-socket.service';

@Component({
  selector: 'app-lobby',
  imports: [RoomsListComponent],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.scss',
})
export class LobbyComponent implements OnInit, OnDestroy {
  private modal = inject(ModalService);
  private service = inject(RoomService);
  private ws = inject(WebSocketService);

  private wsSub?: StompSubscription;

  /** Signal to hold all rooms in the lobby */
  rooms = signal<RoomDto[]>([]);

  ngOnInit() {
    this.loadRooms();
    this.connectWebSocket();
    this.loadCurrentRoom();
  }

  /** Load rooms from backend */
  private async loadRooms() {
    const result = await this.service.getRooms();
    if (result?.content) {
      this.rooms.set(result.content);
    }
  }

  /** Fetch the current room for the user */
  private async loadCurrentRoom() {
    const currentRoom = await this.service.getCurrentRoom();
    if (currentRoom) {
      console.log('User is already in a room:', currentRoom);
      // TODO: redirect to room page if already in a room
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
