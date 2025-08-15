import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AppelOffreService } from '../../../core/services/appel-offre.service';

@Component({
  selector: 'app-appel-offre-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './appel-offre-form.component.html',
  styleUrls: ['./appel-offre-form.component.css']
})
export class AppelOffreFormComponent implements OnInit {
  appelOffreForm!: FormGroup;
  isEditMode = false;
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
    this.initForm();
    this.checkEditMode();
  }

  private initForm(): void {
    this.appelOffreForm = this.fb.group({
      titre: ['', [Validators.required]],
      description: [''],
      dateLimite: ['', [Validators.required]],
      budgetEstimatif: [0, [Validators.min(0)]],
      statut: ['OUVERT', [Validators.required]],
      // fournisseurIds: [[]] // This would be for a multi-select component
    });
  }

  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEditMode = true;
        this.appelOffreId = +id;
        this.isLoading = true;
        this.appelOffreService.getAppelOffreById(this.appelOffreId).subscribe({
          next: (appelOffre) => {
            const formData = {
              ...appelOffre,
              dateLimite: appelOffre.dateLimite ? new Date(appelOffre.dateLimite).toISOString().split('T')[0] : ''
            };
            this.appelOffreForm.patchValue(formData);
            this.isLoading = false;
          },
          error: () => this.handleError('Erreur lors du chargement de l\'appel d\'offres.')
        });
      }
    });
  }

  onSubmit(): void {
    if (this.appelOffreForm.invalid) return;
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
