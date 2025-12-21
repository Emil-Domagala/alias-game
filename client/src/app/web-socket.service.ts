import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private stompClient?: Stomp.Client;

  connect(token?: string): void {
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect(
      token ? {Authorization: `Bearer ${token}`} : {},
      () => console.log('WS connected')
    );
  }

  subscribe(destination: string, callback: (msg: any) => void): Stomp.Subscription | undefined {
    return this.stompClient?.subscribe(destination, message =>
      callback(JSON.parse(message.body))
    );
  }

  disconnect(): void {
    this.stompClient?.disconnect(() => console.log('WS disconnected'));
  }
}
