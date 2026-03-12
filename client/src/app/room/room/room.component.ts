import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WebSocketService } from '../../web-socket.service';
import {JsonPipe} from '@angular/common';
import {StompSubscription} from '@stomp/stompjs';
import {RoomService} from '../room.service';

interface RoomEvent {
  type: string;
  payload: any;
}

@Component({
  selector: 'app-room',
  standalone: true,
  templateUrl: './room.component.html',
  imports: [
    JsonPipe
  ],
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit, OnDestroy {
  private wsSub?: StompSubscription;
  roomId!: string;

  roomEvents: RoomEvent[] = [];
  ws = inject(WebSocketService);
  route = inject(ActivatedRoute);
  roomService = inject(RoomService);

  ngOnInit() {
    // Get room ID from route
    this.roomId = this.route.snapshot.paramMap.get('id')!;

    // Connect if not connected
    this.ws.connect();

    // Subscribe to room topic
    this.wsSub = this.ws.subscribe(`/topic/room/${this.roomId}`, (msg: RoomEvent) => {
      console.log('Room event:', msg);
      this.roomEvents.push(msg);
    });
  }

  joinRoom() {
    // Example: call your REST endpoint or WS command to join
    console.log(`Joining room ${this.roomId}`);
    // e.g., roomService.joinRoom(this.roomId, user)
  }

  leaveRoom() {
    // Example: send leave request to backend
    console.log(`Leaving room ${this.roomId}`);
    this.roomService.leaveRoom(this.roomId)
  }

  ngOnDestroy() {
    this.leaveRoom();
    this.wsSub?.unsubscribe();
    this.ws.disconnect();
  }
}
