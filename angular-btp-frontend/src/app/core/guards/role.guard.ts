import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, filter, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {

    // Get required roles from route data
    const requiredRoles = route.data['roles'] as string[];

    return this.authService.currentUser$.pipe(
      // Wait for a valid user object with roles before making a decision
      filter(user => user !== null && user.roles !== undefined && user.roles.length > 0),
      take(1),
      map(user => {
        // Check if user has at least one of the required roles
        const hasRole = requiredRoles.some(role => user!.roles.includes(role));
        
        if (hasRole) {
          return true;
        } else {
          // User doesn't have required role, redirect to a page they can access
          console.warn(`Access denied: User does not have required roles: ${requiredRoles.join(', ')}`);
          // Redirect to a safe page based on their actual role
          return this.router.createUrlTree(['/projets']);
        }
      })
    );
  }
}
