import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../../environments/environment'; // IDE might flag this

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '${environment.apiUrl}/auth';
  private readonly TOKEN_KEY = 'auth_token';

  private _isLoggedIn$: BehaviorSubject<boolean>;
  isLoggedIn$: Observable<boolean>;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
      this._isLoggedIn$ = new BehaviorSubject<boolean>(!!this.getToken());
    } else {
      this._isLoggedIn$ = new BehaviorSubject<boolean>(false);
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

  // --- START OF FIX ---
  // The method signature now includes all required fields for registration.
  register(userData: { username: string; password: string; firstName: string; lastName: string }): Observable<any> {
    // The userData object, which contains all the fields, is now sent in the POST request.
    return this.http.post(`${this.apiUrl}/register`, userData).pipe(
      tap((response: any) => {
        if (response && response.token) {
          this.setToken(response.token);
          this._isLoggedIn$.next(true);
        }
      })
    );
  }
  // --- END OF FIX ---

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.TOKEN_KEY);
    }
    this._isLoggedIn$.next(false);
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null;
  }

  private setToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }
}
