
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Reset your password</h2>
      <form (ngSubmit)="submit()">
        <label>New password</label>
        <input type="password" [(ngModel)]="password" name="password" required />
        <label>Confirm password</label>
        <input type="password" [(ngModel)]="confirm" name="confirm" required />
        <button type="submit">Reset Password</button>
      </form>
      <p *ngIf="error" style="color:red">{{error}}</p>
      <p *ngIf="success" style="color:green">Password changed. You can now log in.</p>
    </div>
  `
})
export class ResetPasswordComponent {
  password = '';
  confirm = '';
  token = '';
  error = '';
  success = false;

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
