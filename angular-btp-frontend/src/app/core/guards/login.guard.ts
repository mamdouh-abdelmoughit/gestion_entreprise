import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    return this.authService.isLoggedIn$.pipe(
      take(1),
      map(isLoggedIn => {
        if (isLoggedIn) {
          // If the user IS logged in, DO NOT allow access to the login page.
          // Redirect them to the dashboard instead.
          return this.router.createUrlTree(['/dashboard']);
        } else {
          // If the user is NOT logged in, allow access to the login page.
          return true;
        }
      })
    );
  }
}
