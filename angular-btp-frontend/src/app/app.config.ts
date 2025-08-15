import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideClientHydration } from '@angular/platform-browser';
// --- START OF FIX ---
// Import the necessary providers and your existing interceptor
import { provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor'; // Import YOUR interceptor
// --- END OF FIX ---

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),

    // --- START OF FIX ---
    // Update provideHttpClient to enable DI for interceptors
    provideHttpClient(withFetch(), withInterceptorsFromDi()),

    // Register your existing JwtInterceptor as an HTTP interceptor.
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
    // --- END OF FIX ---
  ]
};
