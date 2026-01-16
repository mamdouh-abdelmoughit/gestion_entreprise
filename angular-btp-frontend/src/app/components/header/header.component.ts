import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model'; // ADD THIS IMPORT

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  // --- START CHANGE ---
  // @Input() userEmail: string = ''; // DELETE THIS LINE
  @Input() user: User | null = null; // ADD THIS LINE
  // --- END CHANGE ---

  constructor(private authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
