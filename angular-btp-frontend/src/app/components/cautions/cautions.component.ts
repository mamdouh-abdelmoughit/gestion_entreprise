import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Caution } from '../../core/models/caution.model';
import { CautionService } from '../../core/services/caution.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-cautions',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">📄 Cautions</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouvelle Caution
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Cautions</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && cautionsPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Type</th>
                <th class="py-2 px-4 text-left">Montant</th>
                <th class="py-2 px-4 text-left">Date d'Échéance</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let caution of cautionsPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ caution.type }}</td>
                <td class="py-2 px-4">{{ caution.montant | currency:'EUR' }}</td>
                <td class="py-2 px-4">{{ caution.dateEcheance | date:'dd/MM/yyyy' }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-green-200 text-green-800': caution.statut === 'ACTIVE',
                          'bg-red-200 text-red-800': caution.statut === 'EXPIREE',
                          'bg-gray-200 text-gray-800': caution.statut === 'CLOTUREE'
                        }">
                    {{ caution.statut }}
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
export class CautionsComponent implements OnInit {
  cautionsPage: Page<Caution> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private cautionService: CautionService) {}

  ngOnInit(): void {
    this.loadCautions();
  }

  loadCautions(page = 0, size = 10, sort = 'dateEcheance,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.cautionService.getAllCautions(page, size, sort).subscribe({
      next: (data) => {
        this.cautionsPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des cautions.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}

