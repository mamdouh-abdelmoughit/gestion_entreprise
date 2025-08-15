import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Output() moduleChange = new EventEmitter<string>();

  activeModule = 'dashboard';

  modules = [
    { id: 'dashboard', name: 'Tableau de bord', icon: '📊' },
    { id: 'clients', name: 'Clients', icon: '👤' },
    { id: 'appel-offres', name: 'Appels d\'offres', icon: '📋' },
    { id: 'projets', name: 'Projets/Chantiers', icon: '🏗️' },
    { id: 'cautions', name: 'Cautions', icon: '🏦' },
    { id: 'decomptes', name: 'Décomptes', icon: '💰' },
    { id: 'documents', name: 'Documents', icon: '📁' },
    { id: 'employes', name: 'Employés', icon: '👥' },
    { id: 'affectations', name: 'Affectations', icon: '🔗' }, // Add this line
    { id: 'fournisseurs', name: 'Fournisseurs', icon: '🏪' },
    { id: 'depenses', name: 'Dépenses', icon: '💸' },
    { id: 'users', name: 'Utilisateurs', icon: '🔑' }, // Add this line
    { id: 'roles', name: 'Rôles', icon: '🛡️' } // Add this line
  ];

  selectModule(moduleId: string) {
    this.activeModule = moduleId;
    this.moduleChange.emit(moduleId);
  }
}
