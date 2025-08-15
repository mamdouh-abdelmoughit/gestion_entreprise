import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Observable } from 'rxjs';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, HeaderComponent],
  template: `
    <!-- Main layout for logged-in users -->
    <div *ngIf="isLoggedIn$ | async" class="min-h-screen flex bg-gray-50">
      <app-sidebar (moduleChange)="onModuleChange($event)"></app-sidebar>
      <div class="flex-1 flex flex-col">
        <app-header [userEmail]="userEmail"></app-header>
        <main class="flex-1 p-6 overflow-auto">
          <router-outlet></router-outlet>
        </main>
      </div>
    </div>

    <!-- Login page layout -->
    <div *ngIf="!(isLoggedIn$ | async)">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: []
})
export class AppComponent {
  title = 'angular-btp-frontend';
  isLoggedIn$: Observable<boolean>;
  userEmail = 'admin@btp.com';
  activeModule = 'dashboard';

  constructor(private authService: AuthService) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  onModuleChange(module: string) {
    this.activeModule = module;
  }
}
