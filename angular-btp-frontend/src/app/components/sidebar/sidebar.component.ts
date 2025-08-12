import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="w-64 bg-white shadow-lg">
      <div class="p-6 border-b">
        <h2 class="text-xl font-bold text-blue-600">🏗️ GestionBTP</h2>
        <p class="text-sm text-gray-500">Version Pro</p>
      </div>
      
      <nav class="p-4">
        <ul class="space-y-2">
          <li *ngFor="let module of modules">
            <button
              (click)="selectModule(module.id)"
              class="w-full text-left px-4 py-3 rounded-lg transition-colors flex items-center gap-3"
              [class.bg-blue-100]="activeModule === module.id"
              [class.text-blue-700]="activeModule === module.id"
              [class.text-gray-600]="activeModule !== module.id"
              [class.hover:bg-gray-100]="activeModule !== module.id"
            >
              <span class="text-lg">{{ module.icon }}</span>
              <span class="text-sm">{{ module.name }}</span>
            </button>
          </li>
        </ul>
      </nav>
    </div>
  `,
  styles: []
})
export class SidebarComponent {
  @Output() moduleChange = new EventEmitter<string>();
  
  activeModule = 'dashboard';
  
  modules = [
    { id: 'dashboard', name: 'Tableau de bord', icon: '📊' },
    { id: 'appel-offres', name: 'Appels d\'offres', icon: '📋' },
    { id: 'projets', name: 'Projets/Chantiers', icon: '🏗️' },
    { id: 'cautions', name: 'Cautions', icon: '🏦' },
    { id: 'decomptes', name: 'Décomptes', icon: '💰' },
    { id: 'documents', name: 'Documents', icon: '📁' },
    { id: 'employes', name: 'Employés', icon: '👥' },
    { id: 'fournisseurs', name: 'Fournisseurs', icon: '🏪' },
    { id: 'depenses', name: 'Dépenses', icon: '💸' }
  ];

  selectModule(moduleId: string) {
    this.activeModule = moduleId;
    this.moduleChange.emit(moduleId);
  }
}
