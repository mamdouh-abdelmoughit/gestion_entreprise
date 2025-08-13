import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Employe } from '../../core/models/employe.model';
import { EmployeService } from '../../core/services/employe.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-employes',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">👥 Employés</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouvel Employé
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Employés</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && employesPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Nom</th>
                <th class="py-2 px-4 text-left">Poste</th>
                <th class="py-2 px-4 text-left">Téléphone</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let employe of employesPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ employe.prenom }} {{ employe.nom }}</td>
                <td class="py-2 px-4">{{ employe.poste }}</td>
                <td class="py-2 px-4">{{ employe.telephone }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-green-200 text-green-800': employe.statut === 'ACTIF',
                          'bg-red-200 text-red-800': employe.statut === 'INACTIF'
                        }">
                    {{ employe.statut }}
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
export class EmployesComponent implements OnInit {
  employesPage: Page<Employe> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private employeService: EmployeService) {}

  ngOnInit(): void {
    this.loadEmployes();
  }

  loadEmployes(page = 0, size = 10, sort = 'nom,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.employeService.getAllEmployes(page, size, sort).subscribe({
      next: (data) => {
        this.employesPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des employés.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}

