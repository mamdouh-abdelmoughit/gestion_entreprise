import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
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

  // ===== Startup Verification =====
  /**
   * This method is called by APP_INITIALIZER once before the application starts.
   * It checks for a token in localStorage and validates it with the backend.
   * Based on the result, it sets the initial authentication state.
   */
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
  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          this.setToken(response.token);
          // After a successful login, we must verify the token and get user data.
          this.verifyTokenAndFetchUser().subscribe();
        }
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
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.TOKEN_KEY);
    }
    this._isLoggedIn$.next(false);
    this._currentUser$.next(null);
    this.router.navigate(['/login']);
  }

  // ===== Token Helpers =====

  getToken(): string | null {
    return isPlatformBrowser(this.platformId) ? localStorage.getItem(this.TOKEN_KEY) : null;
  }

  private setToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  // ===== Invitation & Activation API Methods =====

  inviteUser(email: string, username: string, roles: string[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/invite`, { email, username, roles });
  }

  activateAccount(token: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/activate`, { token, password });
  }

  // ===== Password Reset API Methods =====

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
