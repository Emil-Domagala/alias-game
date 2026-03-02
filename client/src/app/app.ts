import {Component, inject, OnInit, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NavbarComponent} from './shared/navbar/navbar.component';
import {FooterComponent} from './shared/footer/footer.component';
import {UserService} from './user/user.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit{
  protected readonly title = signal('client');

  private userService = inject(UserService);

  async ngOnInit() {
    await this.userService.fetchUser();
  }
}
