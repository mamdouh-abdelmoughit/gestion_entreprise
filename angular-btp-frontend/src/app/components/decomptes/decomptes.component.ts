import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Decompte } from '../../core/models/decompte.model';
import { DecompteService } from '../../core/services/decompte.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-decomptes',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">💰 Décomptes</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouveau décompte
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Décomptes Récents</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && decomptesPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Numéro</th>
                <th class="py-2 px-4 text-left">Date</th>
                <th class="py-2 px-4 text-left">Montant Total</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let decompte of decomptesPage.content" class="border-b">
                <td class="py-2 px-4">{{ decompte.numero }}</td>
                <td class="py-2 px-4">{{ decompte.dateDecompte | date:'dd/MM/yyyy' }}</td>
                <td class="py-2 px-4">{{ decompte.montantTotal | currency:'EUR' }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-yellow-200 text-yellow-800': decompte.statut === 'EN_ATTENTE',
                          'bg-green-200 text-green-800': decompte.statut === 'VALIDE',
                          'bg-red-200 text-red-800': decompte.statut === 'REJETE'
                        }">
                    {{ decompte.statut }}
                  </span>
                </td>
                <td class="py-2 px-4">
                  <button class="text-blue-600 hover:underline">Détails</button>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- TODO: Add pagination controls -->
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class DecomptesComponent implements OnInit {
  decomptesPage: Page<Decompte> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private decompteService: DecompteService) {}

  ngOnInit(): void {
    this.loadDecomptes();
  }

  loadDecomptes(page = 0, size = 10, sort = 'dateDecompte,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.decompteService.getAllDecomptes(page, size, sort).subscribe({
      next: (data) => {
        this.decomptesPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des décomptes.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}
