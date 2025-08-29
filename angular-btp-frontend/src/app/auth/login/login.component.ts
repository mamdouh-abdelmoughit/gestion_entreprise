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
  errorMessage: string | null = null;
  showForgotPassword = false; // Add this state variable
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]], // Added email validator for the login/forgot password flow
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Veuillez saisir des informations valides.';
      return;
    }
    this.errorMessage = null;
    this.authService.login(this.loginForm.value).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        this.errorMessage = 'Nom d\'utilisateur ou mot de passe incorrect.';
        console.error('Login failed', err);
      }
    });
  }

onForgotPassword() {
  const emailControl = this.loginForm.get('username');

  // Check if the control exists and is valid before proceeding
  if (emailControl?.invalid) {
    this.errorMessage = "Veuillez entrer une adresse email valide.";
    return;
  }
  
  // Safely access the value using optional chaining
  const email = emailControl?.value;

  if (!email) {
    // This case should not be reached due to the invalid check above, but it's a good safeguard.
    this.errorMessage = "Veuillez entrer votre email pour réinitialiser.";
    return;
  }

  this.errorMessage = null; // Clear previous errors
  this.isLoading = true; // Start loading spinner


  this.authService.requestPasswordReset(email).subscribe({
    next: () => {
      this.isLoading = false; // Stop loading spinner on success
      alert("Un lien de réinitialisation a été envoyé à votre email.");
      this.showForgotPassword = false; // Go back to login form after success
      this.loginForm.reset();
    },
    error: (err) => {
      this.isLoading = false; // Stop loading spinner on error
      this.errorMessage = "Impossible d’envoyer l’email. Vérifiez l'adresse ou réessayez plus tard.";
      console.error('Forgot password failed', err);
    }
  });
}

  toggleForgotPassword() {
    this.showForgotPassword = !this.showForgotPassword;
    this.errorMessage = null;
  }
}