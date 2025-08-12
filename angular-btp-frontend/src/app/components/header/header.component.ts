import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  template: `
    <header class="bg-white shadow-sm border-b px-6 py-4">
      <div class="flex justify-between items-center">
        <div>
          <h1 class="text-2xl font-semibold text-gray-800">
            Gestion BTP & Marchés Publics
          </h1>
          <p class="text-sm text-gray-500">
            Plateforme complète pour entreprises de BTP au Maroc
          </p>
        </div>
        
        <div class="flex items-center gap-4">
          <div class="text-right">
            <p className="text-sm font-medium text-gray-700">
              {{ userEmail || 'Utilisateur' }}
            </p>
            <p class="text-xs text-gray-500">Administrateur</p>
          </div>
          <button class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors">
            Déconnexion
          </button>
        </div>
      </div>
    </header>
  `,
  styles: []
})
export class HeaderComponent {
  @Input() userEmail: string = '';
}
