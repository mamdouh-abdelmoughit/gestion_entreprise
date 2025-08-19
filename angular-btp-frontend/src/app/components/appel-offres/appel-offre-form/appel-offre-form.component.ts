import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AppelOffreService } from '../../../core/services/appel-offre.service';
import {AppelOffre} from "../../../core/models/appel-offre.model";


// --- START OF THE DEFINITIVE FIX ---
// This decorator ensures that the component uses the external HTML file.
@Component({
  selector: 'app-appel-offre-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './appel-offre-form.component.html', // This line is the most important one.
  styleUrls: ['./appel-offre-form.component.css']
})
// --- END OF THE DEFINITIVE FIX ---
export class AppelOffreFormComponent implements OnInit {
  appelOffreForm!: FormGroup| undefined;
  isEditMode = false;
  isDetailsMode = false;
  appelOffreId: number | null = null;
  error: string | null = null;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private appelOffreService: AppelOffreService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}



  ngOnInit(): void {
    // We will now call checkEditMode directly.
    // It will handle initializing the form at the correct time.
    this.checkMode();
  }

  // This is the new, combined logic
  private checkMode(): void {
  const id = this.route.snapshot.paramMap.get('id');
  const url = this.route.snapshot.url.map(segment => segment.path);

  if (id && !isNaN(+id)) {
    this.appelOffreId = +id;
    this.isLoading = true;

    // Detect mode from the URL path
    if (url.includes('edit')) {
      this.isEditMode = true;
    } else if (url.includes('details')) {
      this.isDetailsMode = true;
    }

    this.appelOffreService.getAppelOffreById(this.appelOffreId).subscribe({
      next: (appelOffre) => {
        // Pre-fill form
        this.initForm(appelOffre);
        this.isLoading = false;

        // Disable form if details mode
        if (this.isDetailsMode) {
          if (this.appelOffreForm) {
            this.appelOffreForm.disable();
          }
        }
      },
      error: () => this.handleError("Erreur lors du chargement de l'appel d'offres.")
    });

  } else {
    // --- CREATE MODE ---
    this.isEditMode = false;
    this.isDetailsMode = false;
    this.initForm();  // empty form
    this.isLoading = false;
  }
}


  // The initForm method now accepts optional data to pre-fill the form
  private initForm(data?: AppelOffre): void {
    this.appelOffreForm = this.fb.group({
      numero: [data?.numero || '', [Validators.required]],
      titre: [data?.titre || '', [Validators.required]],
      maitreDOuvrage: [data?.maitreDOuvrage || '', [Validators.required]],
      description: [data?.description || ''],
      datePublication: [data ? new Date(data.datePublication).toISOString().split('T')[0] : '', [Validators.required]],
      dateLimite: [data ? new Date(data.dateLimite).toISOString().split('T')[0] : '', [Validators.required]],
      budgetEstimatif: [data?.budgetEstimatif ?? 0, [Validators.min(0)]],
      statut: [data?.statut || 'EN_COURS', [Validators.required]],
    });
  }
  onSubmit(): void {
    // --- START OF THE FINAL FIX ---
    // 1. Guard Clause: If the form doesn't exist for some reason, do nothing.
    // This satisfies the TypeScript compiler.
    if (!this.appelOffreForm) {
      return;
    }
    // --- END OF THE FINAL FIX ---

    // 2. Now that TypeScript knows the form exists, the rest of the code is valid.
    if (this.appelOffreForm.invalid) {
      // You can also add a user-facing error message here if you want
      // this.error = "Veuillez corriger les erreurs dans le formulaire.";
      return;
    }

    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.appelOffreService.updateAppelOffre(this.appelOffreId!, this.appelOffreForm.value)
      : this.appelOffreService.createAppelOffre(this.appelOffreForm.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/appel-offres']),
      error: () => this.handleError('Erreur lors de la sauvegarde.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
