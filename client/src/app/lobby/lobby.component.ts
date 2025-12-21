import {Component, OnDestroy, OnInit} from '@angular/core';
import {WebSocketService} from '../web-socket.service';
import {Subscription} from 'stompjs';

@Component({
  selector: 'app-lobby',
  imports: [],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.scss'
})
export class LobbyComponent implements OnInit, OnDestroy {
  private wsSub?: Subscription;

  constructor(private ws: WebSocketService) {}

  ngOnInit() {
    this.ws.connect();
    this.wsSub = this.ws.subscribe('/topic/lobby', msg => {
      console.log('Lobby event:', msg);
    });
  }

  ngOnDestroy() {
    this.wsSub?.unsubscribe();
    this.ws.disconnect();
  }
}
