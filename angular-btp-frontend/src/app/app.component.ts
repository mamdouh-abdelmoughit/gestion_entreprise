import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Observable } from 'rxjs';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { AuthService } from './core/services/auth.service';
import {ToastComponent} from "./shared/toast/toast.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ToastComponent],
  templateUrl: './app.component.html'
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
