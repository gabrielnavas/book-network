import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {TokenService} from "./services/token/token.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  token: string = ''

  constructor(
    private tokenService: TokenService
  ) {
    this.token = this.tokenService.token;
  }
}
