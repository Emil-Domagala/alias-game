import {Component, Input, computed, inject} from '@angular/core';
import {ChatMessageWithState} from '../../message.interface';
import {UserService} from '../../../user/user.service';

@Component({
  selector: 'app-message',
  standalone: true,
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],

})
export class MessageComponent {
  private userService = inject(UserService);
  user = this.userService.user;
  @Input() message!: ChatMessageWithState;

  isOwnMessage = computed(() => this.message.sender?.id === this.user()?.id);

  formattedDate = computed(() => {
    const date = new Date(this.message.createdAt);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  });
}
