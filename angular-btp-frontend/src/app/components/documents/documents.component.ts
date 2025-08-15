import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Document } from '../../core/models/document.model';
import { DocumentService } from '../../core/services/document.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css']
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
  deleteDocument(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce document ?')) {
      this.documentService.deleteDocument(id).subscribe({
        next: () => {
          console.log('Document supprimé');
          this.loadDocuments(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression du document.';
          console.error(err);
        }
      });
    }
  }
}

