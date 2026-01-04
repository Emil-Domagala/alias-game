import {Component, effect, input} from '@angular/core';
import {Room} from '../../room/room.interface';
import {RoomsListItemComponent} from './rooms-list-item/rooms-list-item.component';

@Component({
  selector: 'app-rooms-list',
  imports: [
    RoomsListItemComponent
  ],
  templateUrl: './rooms-list.component.html',
  styleUrl: './rooms-list.component.scss',
})
export class RoomsListComponent {
rooms = input.required<Room[]>()

  constructor() {
    effect(() => {
      console.log('rooms updated:', this.rooms());
    });
  }
}
