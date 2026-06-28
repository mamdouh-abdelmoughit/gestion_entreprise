import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthService } from '../services/auth.service';
import { NotificationService } from '../services/notification.service';
import { environment } from '../../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    // Use the robust check against the environment apiUrl to determine if the token should be added.
    const isApiUrl = request.url.startsWith(environment.apiUrl);

    if (token && isApiUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    // Pass the request along and add the error handling pipe.
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // This block will run for any HTTP error received from the backend.

        let errorMessage = 'Une erreur inattendue est survenue.'; // Default message

        // Provide more specific messages based on the error status.
        if (error.error && typeof error.error.message === 'string') {
          errorMessage = error.error.message;
        } else if (error.status === 401) {
          const isAuthEndpoint = request.url.includes('/auth/');
          if (isAuthEndpoint) {
            // Wrong credentials — let the component handle the message.
            errorMessage = '';
          } else {
            errorMessage = 'Session expirée. Veuillez vous reconnecter.';
            this.authService.logout();
          }
        } else if (error.status === 403) {
          errorMessage = 'Accès refusé. Vous n\'avez pas les permissions nécessaires.';
        } else if (error.status === 404) {
          errorMessage = 'La ressource demandée n\'a pas été trouvée.';
        }

        // Display the final error message in a user-friendly toast notification.
        if (errorMessage) {
          this.notificationService.show(errorMessage, 'error');
        }

        // Rethrow the error so that any component-level error handling can still catch it.
        return throwError(() => error);
      })
    );
  }
}
