import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-depenses',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="space-y-6">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">💸 Dépenses</h2>
        <p class="text-gray-600">Suivi et gestion des dépenses et coûts du projet</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div class="bg-white rounded-lg shadow-sm p-6">
          <h3 class="text-lg font-semibold mb-4">Récapitulatif</h3>
          <div class="space-y-2">
            <div class="flex justify-between">
              <span class="text-gray-600">Total dépenses:</span>
              <span class="font-medium">2,450,000 DH</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Ce mois:</span>
              <span class="font-medium text-blue-600">345,000 DH</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Budget restant:</span>
              <span class="font-medium text-green-600">1,200,000 DH</span>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-sm p-6">
          <h3 class="text-lg font-semibold mb-4">Actions</h3>
          <div class="space-y-2">
            <button class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Nouvelle dépense
            </button>
            <button class="w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">
              Rapport détaillé
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class DepensesComponent {}
