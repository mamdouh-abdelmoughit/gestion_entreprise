import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cautions',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="space-y-6">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">🏦 Cautions</h2>
        <p class="text-gray-600">Gestion des cautions et garanties bancaires</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div class="bg-white rounded-lg shadow-sm p-6">
          <h3 class="text-lg font-semibold mb-4">Statistiques</h3>
          <div class="space-y-2">
            <div class="flex justify-between">
              <span class="text-gray-600">Total cautions:</span>
              <span class="font-medium">32</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Actives:</span>
              <span class="font-medium text-blue-600">18</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Libérées:</span>
              <span class="font-medium text-green-600">14</span>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-sm p-6">
          <h3 class="text-lg font-semibold mb-4">Actions rapides</h3>
          <div class="space-y-2">
            <button class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Nouvelle caution
            </button>
            <button class="w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">
              Voir toutes les cautions
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class CautionsComponent {}
