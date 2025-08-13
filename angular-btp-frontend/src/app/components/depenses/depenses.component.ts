import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Depense } from '../../core/models/depense.model';
import { DepenseService } from '../../core/services/depense.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-depenses',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">💸 Dépenses</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouvelle Dépense
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Dépenses</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && depensesPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Description</th>
                <th class="py-2 px-4 text-left">Catégorie</th>
                <th class="py-2 px-4 text-left">Date</th>
                <th class="py-2 px-4 text-right">Montant</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let depense of depensesPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ depense.description }}</td>
                <td class="py-2 px-4">{{ depense.categorie }}</td>
                <td class="py-2 px-4">{{ depense.dateDepense | date:'dd/MM/yyyy' }}</td>
                <td class="py-2 px-4 text-right">{{ depense.montant | currency:'EUR' }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-yellow-200 text-yellow-800': depense.statut === 'EN_ATTENTE',
                          'bg-green-200 text-green-800': depense.statut === 'APPROUVEE',
                          'bg-red-200 text-red-800': depense.statut === 'REJETEE'
                        }">
                    {{ depense.statut }}
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
export class DepensesComponent implements OnInit {
  depensesPage: Page<Depense> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private depenseService: DepenseService) {}

  ngOnInit(): void {
    this.loadDepenses();
  }

  loadDepenses(page = 0, size = 10, sort = 'dateDepense,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.depenseService.getAllDepenses(page, size, sort).subscribe({
      next: (data) => {
        this.depensesPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des dépenses.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}
