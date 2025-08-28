import { provideClientHydration } from '@angular/platform-browser';
import { ApplicationConfig, APP_INITIALIZER } from '@angular/core'; // 1. Import APP_INITIALIZER
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor'; // Import YOUR interceptor
import { routes } from './app.routes';
import {AuthService} from "./core/services/auth.service";

export function initializeApp(authService: AuthService) {
  return () => authService.verifyTokenAndFetchUser();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withFetch(), withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },

    // Add the APP_INITIALIZER provider
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [AuthService],
      multi: true
    }
  ]
};
