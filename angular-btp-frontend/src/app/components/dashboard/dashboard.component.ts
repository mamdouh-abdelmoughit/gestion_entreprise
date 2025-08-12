import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-6">
      <h2 class="text-3xl font-bold text-gray-800 mb-6">Tableau de bord</h2>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-semibold text-gray-700">Projets en cours</h3>
          <p class="text-3xl font-bold text-blue-600">12</p>
        </div>
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-semibold text-gray-700">Appels d'offres</h3>
          <p class="text-3xl font-bold text-green-600">8</p>
        </div>
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-semibold text-gray-700">Budget total</h3>
          <p class="text-3xl font-bold text-purple-600">2.5M DH</p>
        </div>
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-semibold text-gray-700">Employés</h3>
          <p class="text-3xl font-bold text-orange-600">45</p>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-xl font-semibold mb-4">Activité récente</h3>
          <div class="space-y-3">
            <div class="flex items-center gap-3 p-3 bg-gray-50 rounded">
              <span class="text-green-500">✅</span>
              <span class="text-sm">Projet "Résidence Al Amal" - 85% complété</span>
            </div>
            <div class="flex items-center gap-3 p-3 bg-gray-50 rounded">
              <span class="text-blue-500">📋</span>
              <span class="text-sm">Nouvel appel d'offres publié - 5 soumissionnaires</span>
            </div>
            <div class="flex items-center gap-3 p-3 bg-gray-50 rounded">
              <span class="text-purple-500">💰</span>
              <span class="text-sm">Décompte mensuel validé - 250,000 DH</span>
            </div>
          </div>
        </div>

        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-xl font-semibold mb-4">Statistiques</h3>
          <div class="space-y-4">
            <div>
              <div class="flex justify-between text-sm">
                <span>Projets terminés</span>
                <span>75%</span>
              </div>
              <div class="w-full bg-gray-200 rounded-full h-2">
                <div class="bg-blue-600 h-2 rounded-full" style="width: 75%"></div>
              </div>
            </div>
            <div>
              <div class="flex justify-between text-sm">
                <span>Budget utilisé</span>
                <span>60%</span>
              </div>
              <div class="w-full bg-gray-200 rounded-full h-2">
                <div class="bg-green-600 h-2 rounded-full" style="width: 60%"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class DashboardComponent {}
