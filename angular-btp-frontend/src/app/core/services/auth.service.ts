import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest } from '../models/auth/login-request.model';
import { LoginResponse } from '../models/auth/login-response.model';
import { RegisterRequest } from '../models/auth/register-request.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private readonly TOKEN_KEY = 'auth_token';

  private _isLoggedIn$ = new BehaviorSubject<boolean>(!!this.getToken());
  isLoggedIn$ = this._isLoggedIn$.asObservable();

  constructor(private http: HttpClient) { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        this.setToken(response.jwt);
        this._isLoggedIn$.next(true);
      })
    );
  }

  register(userInfo: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userInfo);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this._isLoggedIn$.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
}

