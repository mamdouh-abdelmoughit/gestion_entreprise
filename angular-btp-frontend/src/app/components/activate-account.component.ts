import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './activate-account.component.html'
})
export class ActivateAccountComponent {
  password: string = '';
  confirm: string = '';
  token: string = '';
  error: string = '';
  success: boolean = false;

  constructor(private route: ActivatedRoute, private auth: AuthService, private router: Router) {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

  submit() {
    if (this.password !== this.confirm) {
      this.error = 'Les mots de passe ne correspondent pas';
      return;
    }
    if (this.password.length < 6) {
      this.error = 'Le mot de passe doit contenir au moins 6 caractères';
      return;
    }
    this.error = '';

    this.auth.activateAccountOnly(this.token, this.password).subscribe({
      next: () => {
        // Clear any existing session so the person who opened this link
        // (which may be the admin) does not get auto-redirected to their dashboard.
        this.auth.clearSession();
        this.success = true;
      },
      error: () => this.error = 'Activation échouée. Le lien est peut-être invalide ou expiré.'
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
