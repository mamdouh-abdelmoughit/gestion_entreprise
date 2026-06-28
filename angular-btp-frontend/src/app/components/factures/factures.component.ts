import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FactureService, Facture } from '../../core/services/facture.service';

@Component({
  selector: 'app-factures',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="p-6">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-bold text-secondary-900">Factures</h1>
          <p class="text-sm text-secondary-500 mt-1">Factures clients et fournisseurs</p>
        </div>
        <button (click)="router.navigate(['/factures/new'])"
          class="bg-primary-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-primary-700 flex items-center gap-2">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Nouvelle facture
        </button>
      </div>

      <div class="bg-white rounded-xl border border-gray-200 overflow-hidden">
        <div *ngIf="loading" class="p-12 text-center text-secondary-400">Chargement...</div>
        <table *ngIf="!loading" class="w-full text-sm">
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Numéro</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Projet</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Type</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Émission</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Échéance</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Montant net</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Payé</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Statut</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr *ngFor="let f of factures" class="hover:bg-gray-50">
              <td class="px-4 py-3 font-medium text-secondary-900">{{ f.numero }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ f.projetNom }}</td>
              <td class="px-4 py-3">
                <span [class]="typeClass(f.type)">{{ f.type }}</span>
              </td>
              <td class="px-4 py-3 text-secondary-600">{{ f.dateEmission | date:'dd/MM/yyyy' }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ f.dateEcheance | date:'dd/MM/yyyy' }}</td>
              <td class="px-4 py-3 text-right font-medium">{{ f.montantNet | number:'1.2-2' }} MAD</td>
              <td class="px-4 py-3 text-right text-green-600">{{ f.totalPaye | number:'1.2-2' }} MAD</td>
              <td class="px-4 py-3">
                <span [class]="statutClass(f.statut)">{{ f.statut }}</span>
              </td>
              <td class="px-4 py-3 text-right">
                <div class="flex justify-end gap-2">
                  <button (click)="router.navigate(['/factures/edit', f.id])"
                    class="text-primary-600 hover:text-primary-800 text-xs font-medium">Modifier</button>
                  <button (click)="router.navigate(['/paiements'], { queryParams: { factureId: f.id } })"
                    class="text-blue-600 hover:text-blue-800 text-xs font-medium">Paiements</button>
                </div>
              </td>
            </tr>
            <tr *ngIf="factures.length === 0">
              <td colspan="9" class="px-4 py-12 text-center text-secondary-400">Aucune facture trouvée.</td>
            </tr>
          </tbody>
        </table>
      </div>

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
export class FacturesComponent implements OnInit {
  factures: Facture[] = [];
  loading = true;
  currentPage = 0;
  totalPages = 0;

  constructor(private factureService: FactureService, public router: Router) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.factureService.getAll(this.currentPage, 20).subscribe({
      next: p => { this.factures = p.content; this.totalPages = p.totalPages; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  changePage(p: number): void { this.currentPage = p; this.load(); }

  typeClass(type?: string): string {
    return type === 'CLIENT'
      ? 'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-700'
      : 'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-700';
  }

  statutClass(statut?: string): string {
    const map: Record<string, string> = {
      BROUILLON:          'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-600',
      EMISE:              'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-700',
      PARTIELLEMENT_PAYEE:'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-700',
      PAYEE:              'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-700',
      EN_RETARD:          'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-700',
      ANNULEE:            'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-gray-200 text-gray-500'
    };
    return map[statut ?? ''] ?? map['BROUILLON'];
  }
}
