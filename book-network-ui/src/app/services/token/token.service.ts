import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private TOKEN_KEY = 'token';

  constructor() {
  }

  get token(): string {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (token === null) {
      throw new Error('Token not found.');
    }
    return token;
  }

  set token(token: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
}
