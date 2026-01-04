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
import {CreateRoomRequest} from './CreateRoomRequest.interface';
import {FormFieldComponent} from '../../shared/form/form-field/form-field.component';
import {DIALOG_DATA, DialogRef} from '@angular/cdk/dialog';

@Component({
  selector: 'app-create-room-modal',
  imports: [Field, FormFieldComponent],
  templateUrl: './create-room-modal.component.html',
  styleUrl: './create-room-modal.component.scss',
})
export class CreateRoomModalComponent {
  private dialogRef = inject(DialogRef);
  data = inject(DIALOG_DATA);

  close(result?: any) {
    this.dialogRef.close(result);
  }

  service = inject(RoomService);

  createRoomModel = signal<CreateRoomRequest>({
    maxPlayers: 10,
    minPlayers: 2,
    numberOfTeams: 2,
    name: ''
  });

  createRoom = form(this.createRoomModel, (schemaPath) => {
    // @ts-ignore
    required(schemaPath.maxPlayers, { message: 'Max players is required' });
    min(schemaPath.maxPlayers!, 2, { message: 'Min players must be at least 2' });
    max(schemaPath.maxPlayers!, 10, { message: 'Max players cannot exceed 10' });

    // @ts-ignore
    required(schemaPath.minPlayers, { message: 'Min players is required' });
    min(schemaPath.minPlayers!, 2, { message: 'Min players must be at least 2' });
    max(schemaPath.minPlayers!, 10, { message: 'Max players cannot exceed 10' });

    required(schemaPath.name, { message: 'Name is required' });
    minLength(schemaPath.name, 3, { message: 'Name must be at least 3 characters' });
    maxLength(schemaPath.name, 30, { message: 'Name cannot exceed 30 characters' });

    // @ts-ignore
    required(schemaPath.numberOfTeams, { message: 'Number of teams is required' });
    min(schemaPath.numberOfTeams!, 1, { message: 'Number of teams must be at least 1' });
    max(schemaPath.numberOfTeams!, 5, { message: 'Number of teams cannot exceed 5' });
  });

  submit() {
    if (this.createRoom().invalid()) return;

    const payload = this.createRoom().value();
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
