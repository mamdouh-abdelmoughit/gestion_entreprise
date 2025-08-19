import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CautionService } from '../../../core/services/caution.service';
import { ProjetService } from '../../../core/services/projet.service';
import { Projet } from '../../../core/models/projet.model';

@Component({
  selector: 'app-caution-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './caution-form.component.html',
  styleUrls: ['./caution-form.component.css']
})
export class CautionFormComponent implements OnInit {
  cautionForm!: FormGroup;
  isEditMode = false;
  isDetailsMode = false;
  cautionId: number | null = null;
  error: string | null = null;
  isLoading = false;

  projets: Projet[] = []; // For dropdown

  constructor(
    private fb: FormBuilder,
    private cautionService: CautionService,
    private projetService: ProjetService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadProjets();
    this.checkMode();
   
  }

  private initForm(): void {
    this.cautionForm = this.fb.group({
      numero: ['', [Validators.required]], // Add the form control
      type: ['', [Validators.required]],
      banque: ['', [Validators.required]], // Add the form control
      montant: [0, [Validators.required, Validators.min(0)]],
      dateEmission: ['', [Validators.required]],
      dateEcheance: ['', [Validators.required]],
      statut: ['ACTIVE', [Validators.required]],
      projetId: [null]
      // numero is in the entity, but not yet on the form. This will be the next bug.
    });
  }

  private loadProjets(): void {
    this.projetService.getAllProjets(0, 100, 'nom,asc').subscribe(page => this.projets = page.content);
  }

private checkMode(): void {
  const id = this.route.snapshot.paramMap.get('id');
  const url = this.route.snapshot.url.map(segment => segment.path);

  if (id && !isNaN(+id)) {
    this.cautionId = +id;
    this.isLoading = true;

    if (url.includes('edit')) {
      this.isEditMode = true;
    } else if (url.includes('details')) {
      this.isDetailsMode = true;
    }

    this.cautionService.getCautionById(this.cautionId).subscribe({
      next: (caution) => {
        const formData = {
          ...caution,
          dateEmission: caution.dateEmission ? new Date(caution.dateEmission).toISOString().split('T')[0] : '',
          dateEcheance: caution.dateEcheance ? new Date(caution.dateEcheance).toISOString().split('T')[0] : ''
        };

        this.cautionForm.patchValue(formData);
        this.isLoading = false;

        // disable form fields if in details mode
        if (this.isDetailsMode) {
          this.cautionForm.disable();
        }
      },
      error: () => this.handleError('Erreur lors du chargement de la caution.')
    });

  } else {
    // --- CREATE MODE ---
    this.isEditMode = false;
    this.isDetailsMode = false;
    this.isLoading = false;
  }
}


  onSubmit(): void {
    if (this.cautionForm.invalid) return;
    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.cautionService.updateCaution(this.cautionId!, this.cautionForm.value)
      : this.cautionService.createCaution(this.cautionForm.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/cautions']),
      error: () => this.handleError('Erreur lors de la sauvegarde de la caution.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
