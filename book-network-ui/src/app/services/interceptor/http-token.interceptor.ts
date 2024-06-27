import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {TokenService} from "../token/token.service";

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const permitAll = ['authenticate', 'register', 'activation-account']
  const anyPermit = permitAll.some(permit => req.url.endsWith(permit));
  if (anyPermit) {
    return next(req);
  }

  const tokenService = inject(TokenService)
  if (tokenService.token) {
    const token = tokenService.token;
    const authReq = req.clone({
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
      })
    })
    return next(authReq);
  }

  return next(req);
};
