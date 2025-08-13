import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppelOffre } from '../../core/models/appel-offre.model';
import { AppelOffreService } from '../../core/services/appel-offre.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-appel-offres',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">📢 Appels d'Offres</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouvel Appel d'Offres
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Appels d'Offres</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && appelOffresPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Titre</th>
                <th class="py-2 px-4 text-left">Date Limite</th>
                <th class="py-2 px-4 text-left">Budget Estimatif</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let appelOffre of appelOffresPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ appelOffre.titre }}</td>
                <td class="py-2 px-4">{{ appelOffre.dateLimite | date:'dd/MM/yyyy' }}</td>
                <td class="py-2 px-4">{{ appelOffre.budgetEstimatif | currency:'EUR' }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-blue-200 text-blue-800': appelOffre.statut === 'OUVERT',
                          'bg-gray-200 text-gray-800': appelOffre.statut === 'FERME',
                          'bg-green-200 text-green-800': appelOffre.statut === 'ATTRIBUE',
                          'bg-red-200 text-red-800': appelOffre.statut === 'ANNULE'
                        }">
                    {{ appelOffre.statut }}
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
export class AppelOffresComponent implements OnInit {
  appelOffresPage: Page<AppelOffre> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private appelOffreService: AppelOffreService) {}

  ngOnInit(): void {
    this.loadAppelOffres();
  }

  loadAppelOffres(page = 0, size = 10, sort = 'dateLimite,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.appelOffreService.getAllAppelOffres(page, size, sort).subscribe({
      next: (data) => {
        this.appelOffresPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = "Erreur lors du chargement des appels d'offres.";
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}

