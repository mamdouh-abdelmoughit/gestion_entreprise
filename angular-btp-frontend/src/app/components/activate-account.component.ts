import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [CommonModule, FormsModule],   // ✅ needed for [(ngModel)]
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
      this.error = 'Passwords do not match';
      return;
    }
    this.error = '';
    this.auth.activateAccount(this.token, this.password).subscribe({
      next: () => {
        this.success = true;
        setTimeout(() => this.router.navigate(['/login']), 1200);
      },
      error: () => this.error = 'Activation failed. Token may be invalid or expired.'
    });
  }
}
