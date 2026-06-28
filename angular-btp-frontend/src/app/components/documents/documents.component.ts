import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Document } from '../../core/models/document.model';
import { DocumentService } from '../../core/services/document.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";
import {PaginationComponent} from "../../shared/pagination/pagination.component";
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [CommonModule, RouterLink, PaginationComponent],
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css']
})
export class DocumentsComponent implements OnInit {
  documentsPage: Page<Document> | null = null;
  isLoading = true;
  error: string | null = null;
  currentUser: User | null = null;

  constructor(
    private documentService: DocumentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => this.currentUser = user);
    this.loadDocuments();
  }

  isAdmin(): boolean {
    return this.currentUser?.roles?.includes('ROLE_ADMIN') ?? false;
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
  onPageChange(newPage:number){
    this.loadDocuments(newPage);
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
viewDocument(id: number): void {
  this.documentService.viewDocument(id).subscribe({
    next: (blob) => {
      const fileURL = URL.createObjectURL(blob);
      window.open(fileURL, '_blank');
    },
    error: (err) => {
      console.error('Error viewing document:', err);
      alert('Impossible de visualiser le document.');
    }
  });
}

downloadDocument(id: number, filename: string): void {
  this.documentService.downloadDocument(id).subscribe({
    next: (blob) => {
      const a = document.createElement('a');
      const url = window.URL.createObjectURL(blob);
      a.href = url;
      a.download = filename; // Use document name for download
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    },
    error: (err) => {
      console.error('Error downloading document:', err);
      alert('Impossible de télécharger le document.');
    }
  });
}

}

