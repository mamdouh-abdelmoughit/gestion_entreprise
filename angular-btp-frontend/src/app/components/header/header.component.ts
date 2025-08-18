import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service'; // 1. Import AuthService

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  @Input() userEmail: string = '';

  constructor(private authService: AuthService) {} // 2. Inject AuthService

  // 3. Create the logout method
  logout(): void {
    this.authService.logout();
  }
}
