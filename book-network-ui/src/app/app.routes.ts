import {Routes} from '@angular/router';
import {AppComponent} from "./app.component";
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {ActivateAccountComponent} from "./pages/activate-account/activate-account.component";

export const routes: Routes = [{
  path: '', component: AppComponent,
}, {
  path: 'login',
  component: LoginComponent
}, {
  path: 'register',
  component: RegisterComponent
}, {
  path: 'activation-account',
  component: ActivateAccountComponent
}, {
  path: 'books',
  component: AppComponent
}];
