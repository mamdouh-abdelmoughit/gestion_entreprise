import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';
import { Router } from '@angular/router'; // 1. Import Router

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private userApiUrl = `${environment.apiUrl}/users`;

  private inMemoryToken: string | null = null;
  // --- END OF FIX ---

  private _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  isLoggedIn$: Observable<boolean> = this._isLoggedIn$.asObservable();

  private _currentUser$ = new BehaviorSubject<User | null>(null);
  currentUser$: Observable<User | null> = this._currentUser$.asObservable();

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router // 2. Inject Router
  ) {
    // We no longer need the startup check because we never store the token.
  }

  // We don't need authenticateOnStartup anymore.

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response && response.token) {
          this.setToken(response.token); // Store token in memory
          this._isLoggedIn$.next(true);
          // We can optionally fetch user data here if needed, or just let the app proceed
          // For now, let's keep it simple.
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData).pipe(
      tap((response: any) => {
        if (response && response.token) {
          this.setToken(response.token); // Store token in memory
          this._isLoggedIn$.next(true);
        }
      })
    );
  }

  logout(): void {
    // --- START OF FIX ---
    // Clear the in-memory token and update state.
    this.inMemoryToken = null;
    this._isLoggedIn$.next(false);
    this._currentUser$.next(null);
    // Forcefully navigate to the login page to ensure a clean state.
    this.router.navigate(['/login']);
    // --- END OF FIX ---
  }

  getToken(): string | null {
    // --- START OF FIX ---
    // Get the token from memory instead of localStorage.
    return this.inMemoryToken;
    // --- END OF FIX ---
  }

  private setToken(token: string): void {
    // --- START OF FIX ---
    // Set the token in memory instead of localStorage.
    this.inMemoryToken = token;
    // --- END OF FIX ---
  }
}
