import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DecompteService } from '../../../core/services/decompte.service';
import { ProjetService } from '../../../core/services/projet.service';
import { Projet } from '../../../core/models/projet.model';

@Component({
  selector: 'app-decompte-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './decompte-form.component.html',
  styleUrls: ['./decompte-form.component.css']
})
export class DecompteFormComponent implements OnInit {
  decompteForm!: FormGroup;
  isEditMode = false;
  decompteId: number | null = null;
  error: string | null = null;
  isLoading = false;

  projets: Projet[] = [];

  constructor(
    private fb: FormBuilder,
    private decompteService: DecompteService,
    private projetService: ProjetService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadProjets();
    this.checkEditMode();
  }

  private initForm(): void {
    this.decompteForm = this.fb.group({
      numero: ['', [Validators.required]],
      dateDecompte: ['', [Validators.required]],
      montantTotal: [0, [Validators.required, Validators.min(0)]],
      description: [''],
      statut: ['EN_ATTENTE', [Validators.required]],
      projetId: [null, [Validators.required]]
    });
  }

  private loadProjets(): void {
    this.projetService.getAllProjets(0, 100, 'nom,asc').subscribe(page => this.projets = page.content);
  }

  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEditMode = true;
        this.decompteId = +id;
        this.isLoading = true;
        this.decompteService.getDecompteById(this.decompteId).subscribe({
          next: (decompte) => {
            const formData = {
              ...decompte,
              dateDecompte: decompte.dateDecompte ? new Date(decompte.dateDecompte).toISOString().split('T')[0] : ''
            };
            this.decompteForm.patchValue(formData);
            this.isLoading = false;
          },
          error: () => this.handleError('Erreur lors du chargement du décompte.')
        });
      }
    });
  }

  onSubmit(): void {
    if (this.decompteForm.invalid) return;
    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.decompteService.updateDecompte(this.decompteId!, this.decompteForm.value)
      : this.decompteService.createDecompte(this.decompteForm.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/decomptes']),
      error: () => this.handleError('Erreur lors de la sauvegarde du décompte.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
