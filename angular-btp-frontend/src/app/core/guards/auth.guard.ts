import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, combineLatest } from 'rxjs';
import { map, filter, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {

    // Wait for the auth service to have a definitive state
    // Either we have a logged in user, or we've confirmed no user is logged in
    return combineLatest([
      this.authService.isLoggedIn$,
      this.authService.currentUser$
    ]).pipe(
      // Wait until we have a consistent state: either logged in with user, or logged out with no user
      filter(([isLoggedIn, user]) => {
        // Logged in with user loaded
        if (isLoggedIn && user !== null) return true;
        // Not logged in and no token in storage
        if (!isLoggedIn && !this.authService.getToken()) return true;
        // Still loading...
        return false;
      }),
      take(1),
      map(([isLoggedIn, user]) => {
        if (isLoggedIn && user) {
          return true;
        } else {
          return this.router.createUrlTree(['/login']);
        }
      })
    );
  }
}
