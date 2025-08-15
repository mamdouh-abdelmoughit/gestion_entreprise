import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DocumentService } from '../../../core/services/document.service';
// Note: File upload logic is more complex and often requires a dedicated upload service.
// This is a simplified version.

@Component({
  selector: 'app-document-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './document-form.component.html',
  styleUrls: ['./document-form.component.css']
})
export class DocumentFormComponent implements OnInit {
  documentForm!: FormGroup;
  selectedFile: File | null = null;
  error: string | null = null;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private documentService: DocumentService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.documentForm = this.fb.group({
      nom: ['', [Validators.required]],
      description: [''],
      type: ['AUTRE', [Validators.required]],
      // We handle the file separately, not in the form group
    });
  }

  onFileSelected(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList && fileList.length > 0) {
      this.selectedFile = fileList[0];
    }
  }

  onSubmit(): void {
    if (this.documentForm.invalid || !this.selectedFile) {
      this.error = "Veuillez remplir tous les champs et sélectionner un fichier.";
      return;
    }
    this.isLoading = true;
    this.error = null;

    // In a real app, you would use a dedicated service to upload the file to your backend.
    // The backend would return the file path/URL. Then you would save the document metadata.
    // For now, we'll simulate this by creating a mock document object.

    const documentMetadata = {
      ...this.documentForm.value,
      chemin: `uploads/${this.selectedFile.name}`,
      taille: this.selectedFile.size,
      dateUpload: new Date().toISOString()
    };

    this.documentService.createDocument(documentMetadata).subscribe({
      next: () => this.router.navigate(['/documents']),
      error: () => this.handleError('Erreur lors de la sauvegarde du document.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
