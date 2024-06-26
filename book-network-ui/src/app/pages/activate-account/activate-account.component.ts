import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {CodeInputModule} from "angular-code-input";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [CodeInputModule, NgIf],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {
  message = '';
  isOkay = true;
  submitted = false;

  constructor(
    private readonly router: Router,
    private readonly authService: AuthenticationService,
  ) {
  }

  onCodeCompleted(token: string) {
    this.confirmationAccount(token);
  }

  redirectToLogin() {
    this.router.navigate(['login'])
  }

  disableSubmitted() {
    this.submitted = false
  }

  private confirmationAccount(token: string) {
    this.authService.confirmActivateAccount({
      token
    }).subscribe({
      complete: () => {
        this.message = 'Your account has been successfully activated.\n Now you can proceed to login.';
        this.submitted = true;
        this.isOkay = true;
      },
      error: () => {
        this.message = 'Token has been expired or invalid.';
        this.submitted = true;
        this.isOkay = false;
      }
    })
  }
}
