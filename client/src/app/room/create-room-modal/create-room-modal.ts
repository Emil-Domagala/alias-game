import {Component, inject, signal} from '@angular/core';
import {
  Field,
  form,
  required,
  min,
  max,
  minLength,
  maxLength
} from '@angular/forms/signals';
import {RoomService} from '../room.service';

export interface CreateRoomRequest {
  maxPlayers: number | null;
  minPlayers: number | null;
  numberOfTeams: number | null;
  name: string;
}

@Component({
  selector: 'app-create-room-modal',
  imports: [Field],
  templateUrl: './create-room-modal.html',
  styleUrl: './create-room-modal.scss',
})
export class CreateRoomModal {

  service = inject(RoomService);

  createRoomModel = signal<CreateRoomRequest>({
    maxPlayers: null,
    minPlayers: null,
    numberOfTeams: null,
    name: ''
  });

  createRoomForm = form(this.createRoomModel, (schemaPath) => {
    // maxPlayers
    required(schemaPath.maxPlayers, { message: 'Max players is required' });
    min(schemaPath.maxPlayers, 2, { message: 'Min players must be at least 2' });
    max(schemaPath.maxPlayers, 10, { message: 'Max players cannot exceed 10' });

    // minPlayers
    required(schemaPath.minPlayers, { message: 'Min players is required' });
    min(schemaPath.minPlayers, 2, { message: 'Min players must be at least 2' });
    max(schemaPath.minPlayers, 10, { message: 'Max players cannot exceed 10' });

    // name
    required(schemaPath.name, { message: 'Name is required' });
    minLength(schemaPath.name, 3, {
      message: 'Name must be at least 3 characters'
    });
    maxLength(schemaPath.name, 30, {
      message: 'Name cannot exceed 30 characters'
    });

    // numberOfTeams
    required(schemaPath.numberOfTeams, {
      message: 'Number of teams is required'
    });
    min(schemaPath.numberOfTeams, 1, {
      message: 'Number of teams must be at least 1'
    });
    max(schemaPath.numberOfTeams, 5, {
      message: 'Number of teams cannot exceed 5'
    });
  });

  submit() {
    if (this.createRoomForm().invalid()) return;

    const payload = this.createRoomForm().value();
    console.log('Create room payload:', payload);

    this.service.createRoom(payload)
      .subscribe({
        next: (data) => {
          console.log(data)
        },
        error: err => {
          // handle error
        },
      });
  }
}
