import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faCoffee} from '@fortawesome/free-solid-svg-icons';
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HttpClientModule, FontAwesomeModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  helloWorld = 'hello World';
  faCoffee = faCoffee;
}
