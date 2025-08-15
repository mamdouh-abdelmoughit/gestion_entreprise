import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AffectationEmployeService } from '../../../core/services/affectation-employe.service';
import { ProjetService } from '../../../core/services/projet.service';
import { EmployeService } from '../../../core/services/employe.service';
import { Projet } from '../../../core/models/projet.model';
import { Employe } from '../../../core/models/employe.model';
@Component({
  selector: 'app-affectation-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './affectation-form.component.html'
})
export class AffectationFormComponent implements OnInit {
  form!: FormGroup;
  isEditMode = false;
  affectationId: number | null = null;
  error: string | null = null;
  isLoading = false;
  projets: Projet[] = [];
  employes: Employe[] = [];
  constructor(
    private fb: FormBuilder,
    private affectationService: AffectationEmployeService,
    private projetService: ProjetService,
    private employeService: EmployeService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.initForm();
    this.loadDropdownData();
    this.checkEditMode();
  }
  private initForm(): void {
    this.form = this.fb.group({
      projetId: [null, [Validators.required]],
      employeId: [null, [Validators.required]],
      role: ['', [Validators.required]],
      dateDebut: ['', [Validators.required]],
      dateFin: [''],
      statut: ['ACTIF', [Validators.required]]
    });
  }
  private loadDropdownData(): void {
    this.projetService.getAllProjets(0, 100, 'nom,asc').subscribe(page => this.projets = page.content);
    this.employeService.getAllEmployes(0, 100, 'nom,asc').subscribe(page => this.employes = page.content);
  }
  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEditMode = true;
        this.affectationId = +id;
        this.isLoading = true;
        this.affectationService.getById(this.affectationId).subscribe({
          next: (data) => {
            const formData = {
              ...data,
              dateDebut: data.dateDebut ? new Date(data.dateDebut).toISOString().split('T')[0] : '',
              dateFin: data.dateFin ? new Date(data.dateFin).toISOString().split('T')[0] : ''
            };
            this.form.patchValue(formData);
            this.isLoading = false;
          },
          error: () => this.handleError('Erreur de chargement.')
        });
      }
    });
  }
  onSubmit(): void {
    if (this.form.invalid) return;
    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.affectationService.update(this.affectationId!, this.form.value)
      : this.affectationService.create(this.form.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/affectations']),
      error: () => this.handleError('Erreur de sauvegarde.')
    });
  }
  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
