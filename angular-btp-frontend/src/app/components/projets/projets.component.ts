import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Projet } from '../../core/models/projet.model';
import { ProjetService } from '../../core/services/projet.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-projets',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">🏗️ Projets</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouveau Projet
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Projets</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && projetsPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Nom du Projet</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Date de Début</th>
                <th class="py-2 px-4 text-right">Budget</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let projet of projetsPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ projet.nom }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-blue-200 text-blue-800': projet.statut === 'PLANIFIE',
                          'bg-green-200 text-green-800': projet.statut === 'EN_COURS',
                          'bg-gray-200 text-gray-800': projet.statut === 'TERMINE',
                          'bg-red-200 text-red-800': projet.statut === 'ANNULE'
                        }">
                    {{ projet.statut }}
                  </span>
                </td>
                <td class="py-2 px-4">{{ projet.dateDebut | date:'dd/MM/yyyy' }}</td>
                <td class="py-2 px-4 text-right">{{ projet.budget | currency:'EUR' }}</td>
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
export class ProjetsComponent implements OnInit {
  projetsPage: Page<Projet> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private projetService: ProjetService) {}

  ngOnInit(): void {
    this.loadProjets();
  }

  loadProjets(page = 0, size = 10, sort = 'dateDebut,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.projetService.getAllProjets(page, size, sort).subscribe({
      next: (data) => {
        this.projetsPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des projets.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}
