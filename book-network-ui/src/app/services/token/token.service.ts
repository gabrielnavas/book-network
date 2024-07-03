import {Injectable} from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private TOKEN_KEY = 'token';

  constructor() {
  }

  get token(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  set token(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  private isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    return this.checkExpiryDate(token);
  }

  private checkExpiryDate(token: string): boolean {
    const jwtHelper = new JwtHelperService();
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    console.log(isTokenExpired)
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }
}
