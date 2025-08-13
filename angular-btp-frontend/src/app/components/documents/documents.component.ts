import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Document } from '../../core/models/document.model';
import { DocumentService } from '../../core/services/document.service';
import { Page } from '../../core/models/page.model';

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mx-auto p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">📁 Documents</h2>
        <button class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          Nouveau Document
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-semibold mb-4">Liste des Documents</h3>
        <div *ngIf="isLoading" class="text-center">Chargement...</div>
        <div *ngIf="error" class="text-center text-red-500">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error && documentsPage" class="overflow-x-auto">
          <table class="min-w-full bg-white">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 text-left">Nom</th>
                <th class="py-2 px-4 text-left">Type</th>
                <th class="py-2 px-4 text-left">Date d'Upload</th>
                <th class="py-2 px-4 text-right">Taille (ko)</th>
                <th class="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let document of documentsPage.content" class="border-b">
                <td class="py-2 px-4 font-medium">{{ document.nom }}</td>
                <td class="py-2 px-4">{{ document.type }}</td>
                <td class="py-2 px-4">{{ document.dateUpload | date:'dd/MM/yyyy HH:mm' }}</td>
                <td class="py-2 px-4 text-right">{{ (document.taille / 1024) | number:'1.2-2' }}</td>
                <td class="py-2 px-4">
                  <button class="text-blue-600 hover:underline">Télécharger</button>
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
export class DocumentsComponent implements OnInit {
  documentsPage: Page<Document> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private documentService: DocumentService) {}

  ngOnInit(): void {
    this.loadDocuments();
  }

  loadDocuments(page = 0, size = 10, sort = 'dateUpload,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.documentService.getAllDocuments(page, size, sort).subscribe({
      next: (data) => {
        this.documentsPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des documents.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}

