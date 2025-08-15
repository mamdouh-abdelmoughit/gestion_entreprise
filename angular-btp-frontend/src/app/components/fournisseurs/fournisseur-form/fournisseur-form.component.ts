import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FournisseurService } from '../../../core/services/fournisseur.service';

@Component({
  selector: 'app-fournisseur-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './fournisseur-form.component.html',
  styleUrls: ['./fournisseur-form.component.css']
})
export class FournisseurFormComponent implements OnInit {
  fournisseurForm!: FormGroup;
  isEditMode = false;
  fournisseurId: number | null = null;
  error: string | null = null;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private fournisseurService: FournisseurService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.checkEditMode();
  }

  private initForm(): void {
    this.fournisseurForm = this.fb.group({
      nom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      adresse: [''],
      statut: ['ACTIF', [Validators.required]],
      // Note: 'specialites' is a multi-select or tags input, which is more complex.
      // For a simple form, we'll handle it as a comma-separated string for now.
      specialites: ['']
    });
  }

  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEditMode = true;
        this.fournisseurId = +id;
        this.isLoading = true;
        this.fournisseurService.getFournisseurById(this.fournisseurId).subscribe({
          next: (fournisseur) => {
            const formData = {
              ...fournisseur,
              specialites: fournisseur.specialites ? fournisseur.specialites.join(', ') : ''
            };
            this.fournisseurForm.patchValue(formData);
            this.isLoading = false;
          },
          error: () => this.handleError('Erreur lors du chargement du fournisseur.')
        });
      }
    });
  }

  onSubmit(): void {
    if (this.fournisseurForm.invalid) return;
    this.isLoading = true;
    this.error = null;

    const formValue = this.fournisseurForm.value;
    const dataToSend = {
      ...formValue,
      specialites: formValue.specialites.split(',').map((s: string) => s.trim()).filter((s: string) => s)
    };

    const saveOperation = this.isEditMode
      ? this.fournisseurService.updateFournisseur(this.fournisseurId!, dataToSend)
      : this.fournisseurService.createFournisseur(dataToSend);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/fournisseurs']),
      error: () => this.handleError('Erreur lors de la sauvegarde du fournisseur.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
