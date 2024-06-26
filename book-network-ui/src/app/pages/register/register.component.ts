import {Component} from '@angular/core';
import {RegistrationRequest} from "../../services/models/registration-request";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {faSignInAlt} from "@fortawesome/free-solid-svg-icons";
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FaIconComponent,
    FormsModule,
    NgForOf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registrationRequest: RegistrationRequest = {
    firstname: '',
    email: '',
    lastname: '',
    password: '',
  }
  errorMsg: Array<string> = []
  protected readonly faSignInAlt = faSignInAlt;

  constructor(
    private readonly authService: AuthenticationService,
    private readonly router: Router,
  ) {
  }

  register() {
    this.errorMsg = [];
    this.authService.register({
      body: this.registrationRequest
    }).subscribe({
      error: err => {
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      },
      complete: () => {
        this.router.navigate(['activation-account']);
      }
    })
  }

  login() {
    this.router.navigate(['login']);
  }
}
