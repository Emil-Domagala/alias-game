import {Component, inject} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {AUTH_ROUTES_FULL} from '../../auth/auth.routes';
import {AuthService} from '../../auth/auth.service';
import {UserService} from '../../user/user.service';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLinkActive,
    RouterLink
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  authService = inject(AuthService);
  userService = inject(UserService);

  get isAuthenticated() {
    return this.userService.isAuthenticated;
  }

  async logout() {
    await this.authService.logout();
  }

  protected readonly AUTH_ROUTES_FULL = AUTH_ROUTES_FULL;
}
