import { Injectable, PLATFORM_ID, Inject } from '@angular/core'; // Import PLATFORM_ID and Inject
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common'; // Import isPlatformBrowser

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private readonly TOKEN_KEY = 'auth_token';

  // Initialize _isLoggedIn$ conditionally
  private _isLoggedIn$: BehaviorSubject<boolean>;
  isLoggedIn$: Observable<boolean>;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object // Inject PLATFORM_ID
  ) {
    // Initialize BehaviorSubject based on platform
    if (isPlatformBrowser(this.platformId)) {
      this._isLoggedIn$ = new BehaviorSubject<boolean>(!!this.getToken());
    } else {
      this._isLoggedIn$ = new BehaviorSubject<boolean>(false); // Default to false for SSR
    }
    this.isLoggedIn$ = this._isLoggedIn$.asObservable();
  }

   login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response && response.token) {
          this.setToken(response.token);
          this._isLoggedIn$.next(true);
        }
      })
    );
  }

  register(credentials: { username: string; password: string }): Observable<any> {
      return this.http.post(`${this.apiUrl}/register`, credentials).pipe(
        tap((response: any) => {
          if (response && response.token) {
            this.setToken(response.token);
            this._isLoggedIn$.next(true);
          }
        })
      );
    }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) { // Conditionally remove item
      localStorage.removeItem(this.TOKEN_KEY);
    }
    this._isLoggedIn$.next(false);
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) { // Conditionally get item
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null; // Return null if not in browser
  }

  private setToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) { // Conditionally set item
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }
}
