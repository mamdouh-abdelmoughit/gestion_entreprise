import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AttachementService, Attachement } from '../../core/services/attachement.service';

@Component({
  selector: 'app-attachements',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="p-6">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-bold text-secondary-900">Attachements</h1>
          <p class="text-sm text-secondary-500 mt-1">Suivi des quantités par projet (sans prix)</p>
        </div>
        <button (click)="router.navigate(['/attachements/new'])"
          class="bg-primary-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-primary-700 flex items-center gap-2">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Nouvel attachement
        </button>
      </div>

      <!-- Alert summary banner -->
      <div *ngIf="alertCount > 0"
        class="mb-4 bg-orange-50 border border-orange-200 rounded-lg p-3 flex items-center gap-2 text-orange-700 text-sm">
        <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.07 16.5c-.77.833.192 2.5 1.732 2.5z"/>
        </svg>
        <strong>{{ alertCount }} ligne(s)</strong>&nbsp;avec dépassement de quantité prévue dans vos attachements.
      </div>

      <div class="bg-white rounded-xl border border-gray-200 overflow-hidden">
        <div *ngIf="loading" class="p-12 text-center text-secondary-400">Chargement...</div>

        <table *ngIf="!loading" class="w-full text-sm">
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Numéro</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Projet</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Période</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Date</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Statut</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Alertes</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr *ngFor="let att of attachements" class="hover:bg-gray-50 transition-colors">
              <td class="px-4 py-3 font-medium text-secondary-900">{{ att.numero }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ att.projetNom || '—' }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ att.periode }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ att.dateAttachement | date:'dd/MM/yyyy' }}</td>
              <td class="px-4 py-3">
                <span [class]="statutClass(att.statut)">{{ att.statut }}</span>
              </td>
              <td class="px-4 py-3">
                <span *ngIf="hasAlerte(att)"
                  class="inline-flex items-center gap-1 text-orange-600 text-xs font-medium">
                  <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
                  </svg>
                  Dépassement
                </span>
              </td>
              <td class="px-4 py-3 text-right">
                <div class="flex justify-end gap-2">
                  <button (click)="router.navigate(['/attachements/edit', att.id])"
                    class="text-primary-600 hover:text-primary-800 text-xs font-medium">Modifier</button>
                  <button (click)="delete(att.id!)"
                    class="text-red-500 hover:text-red-700 text-xs font-medium">Supprimer</button>
                </div>
              </td>
            </tr>
            <tr *ngIf="attachements.length === 0">
              <td colspan="7" class="px-4 py-12 text-center text-secondary-400">
                Aucun attachement trouvé.
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div *ngIf="totalPages > 1" class="flex justify-center gap-2 mt-4">
        <button (click)="changePage(currentPage - 1)" [disabled]="currentPage === 0"
          class="px-3 py-1 rounded border text-sm disabled:opacity-40">Précédent</button>
        <span class="px-3 py-1 text-sm text-secondary-600">{{ currentPage + 1 }} / {{ totalPages }}</span>
        <button (click)="changePage(currentPage + 1)" [disabled]="currentPage >= totalPages - 1"
          class="px-3 py-1 rounded border text-sm disabled:opacity-40">Suivant</button>
      </div>
    </div>
  `
})
export class AttachementsComponent implements OnInit {
  attachements: Attachement[] = [];
  loading = true;
  currentPage = 0;
  totalPages = 0;
  alertCount = 0;

  constructor(
    private attachementService: AttachementService,
    public router: Router
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.attachementService.getAll(this.currentPage, 20).subscribe({
      next: page => {
        this.attachements = page.content;
        this.totalPages = page.totalPages;
        this.alertCount = page.content.reduce((acc, a) =>
          acc + (a.lignes?.filter(l => l.alerte).length ?? 0), 0);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  changePage(p: number): void { this.currentPage = p; this.load(); }

  hasAlerte(att: Attachement): boolean {
    return att.lignes?.some(l => l.alerte) ?? false;
  }

  statutClass(statut?: string): string {
    const map: Record<string, string> = {
      BROUILLON: 'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-600',
      SOUMIS:    'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-700',
      VALIDE:    'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-700'
    };
    return map[statut ?? ''] ?? map['BROUILLON'];
  }

  delete(id: number): void {
    if (!confirm('Supprimer cet attachement ?')) return;
    this.attachementService.delete(id).subscribe(() => this.load());
  }
}
