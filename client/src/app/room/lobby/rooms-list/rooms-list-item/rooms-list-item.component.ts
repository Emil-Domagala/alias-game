import {Component, inject, input} from '@angular/core';
import {Room} from '../../../room.interface';
import {RoomService} from '../../../room.service';

@Component({
  selector: 'app-rooms-list-item',
  imports: [],
  templateUrl: './rooms-list-item.component.html',
  styleUrl: './rooms-list-item.component.scss',
})
export class RoomsListItemComponent {
  service = inject(RoomService);
  room = input.required<Room>()

  join(){
    if(!this.room().id) return;
    this.service.joinRoom(this.room().id);
  }
}
