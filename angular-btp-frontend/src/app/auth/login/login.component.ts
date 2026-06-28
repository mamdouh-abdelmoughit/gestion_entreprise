import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  forgotForm!: FormGroup;
  errorMessage: string | null = null;
  showForgotPassword = false;
  isLoading = false;
  forgotSuccess = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    this.errorMessage = null;
    this.isLoading = true;
    this.authService.login(this.loginForm.value).subscribe({
      next: (user) => {
        this.isLoading = false;
        if (user) {
          this.router.navigate(['/dashboard']);
        } else {
          this.errorMessage = "Erreur d'authentification. Veuillez réessayer.";
        }
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = "Nom d'utilisateur ou mot de passe incorrect.";
      }
    });
  }

  onForgotPassword() {
    if (this.forgotForm.invalid) {
      this.forgotForm.markAllAsTouched();
      this.errorMessage = "Veuillez entrer une adresse email valide.";
      return;
    }
    this.errorMessage = null;
    this.isLoading = true;
    const email = this.forgotForm.get('email')!.value;
    this.authService.requestPasswordReset(email).subscribe({
      next: () => {
        this.isLoading = false;
        this.forgotSuccess = true;
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = "Impossible d'envoyer l'email. Verifiez l'adresse ou reessayez plus tard.";
      }
    });
  }

  toggleForgotPassword() {
    this.showForgotPassword = !this.showForgotPassword;
    this.errorMessage = null;
    this.forgotSuccess = false;
    this.forgotForm.reset();
    this.isLoading = false;
  }
}
