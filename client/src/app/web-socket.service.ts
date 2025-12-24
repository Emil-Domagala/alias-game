import { Injectable, signal } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {

  private client?: Client;
  readonly connected = signal(false);

  connect(onConnected?: () => void): void {
    if (this.client?.active) return;

    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',

      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,

      onConnect: () => {
        this.connected.set(true);
        console.log('WS connected');
        onConnected?.();
      },

      onDisconnect: () => {
        this.connected.set(false);
        console.log('WS disconnected');
      },

      onStompError: frame => {
        console.error('STOMP error:', frame.headers['message']);
        console.error(frame.body);
      },
    });

    this.client.activate();
  }

  subscribe<T>(
    destination: string,
    callback: (payload: T) => void
  ): StompSubscription | undefined {
    if (!this.client || !this.connected()) {
      console.warn('STOMP not connected');
      return;
    }

    return this.client.subscribe(destination, (message: IMessage) => {
      callback(JSON.parse(message.body) as T);
    });
  }

  disconnect(): void {
    if (!this.client) return;

    this.client.deactivate();
    this.client = undefined;
  }
}
