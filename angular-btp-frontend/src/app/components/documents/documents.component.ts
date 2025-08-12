import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="space-y-6">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">📁 Documents</h2>
        <p class="text-gray-600">Gestion et stockage des documents du projet</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div class="bg-white rounded-lg shadow-sm p-6">
          <h3 class="text-lg font-semibold mb-4">Statistiques</h3>
          <div class="space-y-2">
            <div class="flex justify-between">
              <span class="text-gray-600">Total documents:</span>
              <span class="font-medium">156</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Récemment ajoutés:</span>
              <span class="font-medium text-blue-600">12</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Partagés:</span>
              <span class="font-medium text-green-600">89</span>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-sm p-6">
          <h3 class="text-lg font-semibold mb-4">Actions</h3>
          <div class="space-y-2">
            <button class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Ajouter document
            </button>
            <button class="w-full px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">
              Rechercher
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class DocumentsComponent {}
