import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-sign-in-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="w-full max-w-md mx-auto">
      <div class="bg-white rounded-lg shadow-lg p-8">
        <div class="text-center mb-6">
          <h2 class="text-2xl font-bold text-gray-800">Connexion</h2>
          <p class="text-gray-600 mt-2">Connectez-vous à votre espace GestionBTP</p>
        </div>
        
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <input 
              type="email" 
              formControlName="username"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="admin@btp.com"
              required
            >
            <!-- Basic validation message example -->
            <div *ngIf="loginForm.get('username')?.invalid && loginForm.get('username')?.touched" class="text-red-500 text-xs mt-1">
              Un email valide est requis.
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Mot de passe
            </label>
            <input 
              type="password" 
              formControlName="password"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="••••••••"
              required
            >
          </div>

          <div *ngIf="errorMessage" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
            <span class="block sm:inline">{{ errorMessage }}</span>
          </div>
          
          <button 
            type="submit"
            [disabled]="loginForm.invalid"
            class="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition duration-200"
          >
            Se connecter
          </button>
        </form>
        
        <div class="mt-4 text-center">
          <p class="text-sm text-gray-600">
            Mot de passe oublié? <a href="#" class="text-blue-600 hover:underline">Contactez l'admin</a>
          </p>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class SignInFormComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
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
}
