import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {

    // Use the isLoggedIn$ observable from your AuthService
    return this.authService.isLoggedIn$.pipe(
      take(1), // Take the latest value and complete
      map(isLoggedIn => {
        if (isLoggedIn) {
          // If the user is logged in, allow access to the route
          return true;
        } else {
          // If the user is not logged in, redirect to the login page
          return this.router.createUrlTree(['/login']);
        }
      })
    );
  }
}
