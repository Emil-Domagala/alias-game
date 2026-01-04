import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {WebSocketService} from '../web-socket.service';
import {StompSubscription} from '@stomp/stompjs';
import {CreateRoomModalComponent} from '../room/create-room-modal/create-room-modal.component';
import {Room} from '../room/room.interface';
import {Subscription} from 'rxjs';
import {RoomService} from '../room/room.service';
import {ModalService} from '../shared/modal/modal.service';
import {RoomsListComponent} from './rooms-list/rooms-list.component';

@Component({
  selector: 'app-lobby',
  imports: [
    RoomsListComponent
  ],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.scss'
})
export class LobbyComponent implements OnInit, OnDestroy {
  private modal = inject(ModalService);
  private service = inject(RoomService);
  private ws = inject(WebSocketService);
  private wsSub?: StompSubscription;

  private roomsSub?: Subscription;

  rooms = signal<Room[]>([]);

  ngOnInit() {
    this.roomsSub = this.service.getRooms().subscribe(result => {
      console.log('result', result)
      this.rooms.set(result.data.content);
    });



    this.ws.connect(() => {
      this.wsSub = this.ws.subscribe('/topic/lobby', msg => {
        console.log('Lobby event:', msg);
      });
    });
  }

  openCreateRoomModal(){
  const ref = this.modal.open(CreateRoomModalComponent);
    ref.closed.subscribe(result => {
      if (result) {
        // refresh rooms or append
        console.log('Room created:', result);
      }
    });
  }

  ngOnDestroy() {
    this.roomsSub?.unsubscribe();
    this.wsSub?.unsubscribe();
    this.ws.disconnect();
  }
}
