import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-sign-in-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="w-full max-w-md mx-auto">
      <div class="bg-white rounded-lg shadow-lg p-8">
        <div class="text-center mb-6">
          <h2 class="text-2xl font-bold text-gray-800">Connexion</h2>
          <p class="text-gray-600 mt-2">Connectez-vous à votre espace GestionBTP</p>
        </div>
        
        <form (ngSubmit)="onSubmit()" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <input 
              type="email" 
              [(ngModel)]="email" 
              name="email"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="admin@btp.com"
              required
            >
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Mot de passe
            </label>
            <input 
              type="password" 
              [(ngModel)]="password" 
              name="password"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="••••••••"
              required
            >
          </div>
          
          <button 
            type="submit"
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
export class SignInFormComponent {
  email = '';
  password = '';

  onSubmit() {
    console.log('Login attempt:', this.email);
    // Authentication logic would go here
  }
}
