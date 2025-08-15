import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DepenseService } from '../../../core/services/depense.service';
import { ProjetService } from '../../../core/services/projet.service';
import { Projet } from '../../../core/models/projet.model';
// Assuming you will create an EmployeService
import { EmployeService } from '../../../core/services/employe.service';
import { Employe } from '../../../core/models/employe.model';


@Component({
  selector: 'app-depense-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './depense-form.component.html',
  styleUrls: ['./depense-form.component.css']
})
export class DepenseFormComponent implements OnInit {
  depenseForm!: FormGroup;
  isEditMode = false;
  depenseId: number | null = null;
  error: string | null = null;
  isLoading = false;

  projets: Projet[] = [];
  employes: Employe[] = [];

  constructor(
    private fb: FormBuilder,
    private depenseService: DepenseService,
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
    this.depenseForm = this.fb.group({
      description: ['', [Validators.required]],
      montant: [0, [Validators.required, Validators.min(0)]],
      dateDepense: ['', [Validators.required]],
      categorie: ['', [Validators.required]],
      statut: ['EN_ATTENTE', [Validators.required]],
      projetId: [null, [Validators.required]],
      employeId: [null]
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
        this.depenseId = +id;
        this.isLoading = true;
        this.depenseService.getDepenseById(this.depenseId).subscribe({
          next: (depense) => {
            const formData = {
              ...depense,
              dateDepense: depense.dateDepense ? new Date(depense.dateDepense).toISOString().split('T')[0] : ''
            };
            this.depenseForm.patchValue(formData);
            this.isLoading = false;
          },
          error: () => this.handleError('Erreur lors du chargement de la dépense.')
        });
      }
    });
  }

  onSubmit(): void {
    if (this.depenseForm.invalid) return;
    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.depenseService.updateDepense(this.depenseId!, this.depenseForm.value)
      : this.depenseService.createDepense(this.depenseForm.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/depenses']),
      error: () => this.handleError('Erreur lors de la sauvegarde de la dépense.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
