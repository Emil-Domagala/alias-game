import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {WebSocketService} from '../web-socket.service';
import {StompSubscription} from '@stomp/stompjs';
import {CreateRoomModal} from '../room/create-room-modal/create-room-modal';

@Component({
  selector: 'app-lobby',
  imports: [
    CreateRoomModal
  ],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.scss'
})
export class LobbyComponent implements OnInit, OnDestroy {
  private wsSub?: StompSubscription;
  ws = inject(WebSocketService);

  ngOnInit() {
    this.ws.connect(() => {
      this.wsSub = this.ws.subscribe('/topic/lobby', msg => {
        console.log('Lobby event:', msg);
      });
    });
  }

  ngOnDestroy() {
    this.wsSub?.unsubscribe();
    this.ws.disconnect();
  }
}
