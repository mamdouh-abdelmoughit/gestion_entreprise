import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],  // ✅ required for ngModel
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent {
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
      this.error = 'Passwords do not match';
      return;
    }
    this.error = '';
    this.auth.resetPassword(this.token, this.password).subscribe({
      next: () => {
        this.success = true;
        setTimeout(() => this.router.navigate(['/login']), 1200);
      },
      error: () => this.error = 'Reset failed. Token may be invalid or expired.'
    });
  }
}
