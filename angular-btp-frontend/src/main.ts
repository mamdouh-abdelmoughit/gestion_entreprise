import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { HTTP_INTERCEPTORS, withInterceptorsFromDi } from '@angular/common/http';
import { JwtInterceptor } from './app/core/interceptors/jwt.interceptor';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()), // Ensure this is present
  { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    provideHttpClient()
  ]
}).catch(err => console.error(err));
