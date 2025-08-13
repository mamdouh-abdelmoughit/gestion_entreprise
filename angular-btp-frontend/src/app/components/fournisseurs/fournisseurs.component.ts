import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Fournisseur } from '../../core/models/fournisseur.model';
import { FournisseurService } from '../../core/services/fournisseur.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-fournisseurs',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">🚚 Fournisseurs</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouveau Fournisseur
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Fournisseurs</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && fournisseursPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Nom</th>
                <th class="py-2 px-4 text-left">Email</th>
                <th class="py-2 px-4 text-left">Téléphone</th>
                <th class="py-2 px-4 text-left">Statut</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let fournisseur of fournisseursPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ fournisseur.nom }}</td>
                <td class="py-2 px-4">{{ fournisseur.email }}</td>
                <td class="py-2 px-4">{{ fournisseur.telephone }}</td>
                <td class="py-2 px-4">
                  <span class="px-2 py-1 text-xs rounded-full"
                        [ngClass]="{
                          'bg-green-200 text-green-800': fournisseur.statut === 'ACTIF',
                          'bg-red-200 text-red-800': fournisseur.statut === 'INACTIF'
                        }">
                    {{ fournisseur.statut }}
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
export class FournisseursComponent implements OnInit {
  fournisseursPage: Page<Fournisseur> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private fournisseurService: FournisseurService) {}

  ngOnInit(): void {
    this.loadFournisseurs();
  }

  loadFournisseurs(page = 0, size = 10, sort = 'nom,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.fournisseurService.getAllFournisseurs(page, size, sort).subscribe({
      next: (data) => {
        this.fournisseursPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des fournisseurs.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}

