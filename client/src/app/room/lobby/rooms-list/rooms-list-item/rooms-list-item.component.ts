import {Component, input} from '@angular/core';
import {Room} from '../../../room.interface';

@Component({
  selector: 'app-rooms-list-item',
  imports: [],
  templateUrl: './rooms-list-item.component.html',
  styleUrl: './rooms-list-item.component.scss',
})
export class RoomsListItemComponent {
  room = input.required<Room>()

  join(){}
}
