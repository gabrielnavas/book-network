import {Component} from '@angular/core';
import {AuthenticateRequest} from "../../services/models/authenticate-request";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {faSignInAlt} from '@fortawesome/free-solid-svg-icons';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FaIconComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authRequest: AuthenticateRequest = {email: '', password: ''}
  errorMsg: Array<String> = []
  faSignInAlt = faSignInAlt;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {
  }

  login() {
    this.errorMsg = [];
    this.authService.authenticate({
      body: {email: this.authRequest.email, password: this.authRequest.password},
    }).subscribe({
      next: value => {
        // TODO: save the value.token localstorage, maybe?
        console.log(value);
        this.router.navigate(['books']);
      },
      error: err => {
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      }
    });
  }

  register() {
    this.router.navigate(['register']);
  }
}
