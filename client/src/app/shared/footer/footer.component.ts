import {Component, inject} from '@angular/core';
import {UserService} from '../../user/user.service';
import {RouterLink} from '@angular/router';
import {AUTH_ROUTES_FULL} from '../../auth/auth.router';

@Component({
  selector: 'app-footer',
  imports: [
    RouterLink
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss',
})
export class FooterComponent {
  private userService = inject(UserService);
  currentYear = new Date().getFullYear();

  get isAuthenticated() {
    return this.userService.isAuthenticated;
  }


  protected readonly AUTH_ROUTES_FULL = AUTH_ROUTES_FULL;
}
