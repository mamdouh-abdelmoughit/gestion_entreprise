import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of, switchMap  } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private userApiUrl = `${environment.apiUrl}/users`;
  private readonly TOKEN_KEY = 'auth_token';

  // These BehaviorSubjects are the single source of truth for the app's auth state.
  private _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this._isLoggedIn$.asObservable();

  private _currentUser$ = new BehaviorSubject<User | null>(null);
  currentUser$ = this._currentUser$.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
  ) {
    // The constructor is now empty. All startup logic is handled by the APP_INITIALIZER.
  }

  // ===== Startup =====
  initializeApp(): Observable<User | null> {
    if (!isPlatformBrowser(this.platformId)) return of(null);
    // Warm up the backend so the first login/register request is instant.
    // Fires-and-forgets the health ping; errors are silently ignored.
    this.http.get(`${environment.apiUrl.replace('/api', '')}/api/health`)
      .pipe(catchError(() => of(null)))
      .subscribe();
    return this.verifyTokenAndFetchUser();
  }

  verifyTokenAndFetchUser(): Observable<User | null> {
    const token = this.getToken();

    if (isPlatformBrowser(this.platformId) && token) {
      // If a token exists, try to fetch the current user to validate it.
      return this.http.get<User>(`${this.userApiUrl}/me`).pipe(
        tap(user => {
          if (user) {
            // Token is valid: update state.
            this._currentUser$.next(user);
            this._isLoggedIn$.next(true);
          } else {
            // Should not happen, but as a safeguard:
            this.logout();
          }
        }),
        catchError(() => {
          // If the /me endpoint fails (e.g., 401 Unauthorized), the token is invalid.
          this.logout();
          return of(null); // Return an empty observable to complete the startup process.
        })
      );
    }

    // If no token exists, ensure the state is logged out and complete the startup.
    this._isLoggedIn$.next(false);
    this._currentUser$.next(null);
    return of(null);
  }

  // ===== Core Auth API Methods =====

  /**
   * Logs in a user and, on success, stores the token and fetches user details.
   */
  login(credentials: { username: string; password: string }): Observable<User | null> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      // We use switchMap to chain the next async operation.
      switchMap(response => {
        if (response && response.token) {
          this.setToken(response.token);
          // The result of this inner observable will become the result of the entire chain.
          return this.verifyTokenAndFetchUser();
        }
        // If no token, log out and return null.
        this.logout();
        return of(null);
      })
    );
  }

  /**
   * Handles public registration for new Admins.
   */
  register(userData: { username: string; password: string; firstName: string; lastName: string; email: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, userData).pipe(
      tap(response => {
        if (response && response.token) {
          this.setToken(response.token);
          this.verifyTokenAndFetchUser().subscribe();
        }
      })
    );
  }

  /**
   * Logs the user out by clearing all stored data and state.
   */
  logout() {
    this.clearSession();
    this.router.navigate(['/login']);
  }
  
  /**
   * Clears the current session without navigating.
   * Used when switching accounts or during activation.
   */
  clearSession(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.TOKEN_KEY);
    }
    this._isLoggedIn$.next(false);
    this._currentUser$.next(null);
  }

  // ===== Token Helpers =====

  getToken(): string | null {
    return isPlatformBrowser(this.platformId) ? localStorage.getItem(this.TOKEN_KEY) : null;
  }

  setToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  // ===== Invitation & Activation API Methods =====

  inviteUser(email: string, username: string, roles: string[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/invite`, { email, username, roles });
  }

  /**
   * Activate account only - NO auto-login.
   * User must login manually after activation.
   * This does NOT touch any existing session (admin can stay logged in).
   */
  activateAccountOnly(token: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/activate`, { token, password });
  }
  
  // Determine the dashboard path based on user role
  getDashboardPathForRoles(roles: string[]): string {
    if (roles.includes('ROLE_ADMIN')) {
      return '/dashboard';
    } else if (roles.includes('ROLE_EMPLOYEE')) {
      return '/dashboard'; // Employees see employee dashboard
    } else if (roles.includes('ROLE_CLIENT')) {
      return '/projets'; // Clients see their projects
    } else if (roles.includes('ROLE_FOURNISSEUR')) {
      return '/appel-offres'; // Fournisseurs see appel d'offres
    }
    return '/dashboard'; // Default
  }

  requestPasswordReset(email: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/forgot-password`, { email });
  }

  resetPassword(token: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/reset-password`, { token, password });
  }

  // ===== Role Helper (Placeholder) =====

  hasRole(role: string): boolean {
    // For a real implementation, you would decode the JWT or check the _currentUser$ object.
    const user = this._currentUser$.getValue();
    return user?.roles.includes(role) ?? false;
  }
}
