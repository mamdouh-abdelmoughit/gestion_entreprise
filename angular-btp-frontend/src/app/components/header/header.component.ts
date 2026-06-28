import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  @Input() userEmail: string = '';
  currentUser: User | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user?.email) {
        this.userEmail = user.email;
      }
    });
  }

  // Get display-friendly role name
  getRoleDisplay(): string {
    if (!this.currentUser?.roles || this.currentUser.roles.length === 0) {
      return 'Utilisateur';
    }
    const role = this.currentUser.roles[0];
    switch (role) {
      case 'ROLE_ADMIN': return 'Administrateur';
      case 'ROLE_EMPLOYEE': return 'Employé';
      case 'ROLE_CLIENT': return 'Client';
      case 'ROLE_FOURNISSEUR': return 'Fournisseur';
      default: return 'Utilisateur';
    }
  }

  // Get role badge color
  getRoleBadgeClass(): string {
    if (!this.currentUser?.roles || this.currentUser.roles.length === 0) {
      return 'bg-secondary-100 text-secondary-700';
    }
    const role = this.currentUser.roles[0];
    switch (role) {
      case 'ROLE_ADMIN': return 'bg-primary-100 text-primary-700';
      case 'ROLE_EMPLOYEE': return 'bg-success-100 text-success-700';
      case 'ROLE_CLIENT': return 'bg-purple-100 text-purple-700';
      case 'ROLE_FOURNISSEUR': return 'bg-accent-100 text-accent-700';
      default: return 'bg-secondary-100 text-secondary-700';
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
