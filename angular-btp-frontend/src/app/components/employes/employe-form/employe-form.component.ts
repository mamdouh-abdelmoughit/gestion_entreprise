import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeService } from '../../../core/services/employe.service';

@Component({
  selector: 'app-employe-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employe-form.component.html',
  styleUrls: ['./employe-form.component.css']
})
export class EmployeFormComponent implements OnInit {
  employeForm!: FormGroup;
  isEditMode = false;
  isDetailsMode = false;
  employeId: number | null = null;
  error: string | null = null;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private employeService: EmployeService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.checkMode();
  }

  private initForm(): void {
    this.employeForm = this.fb.group({
      cin: ['', [Validators.required]], // Add the form control
      nom: ['', [Validators.required]],
      prenom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      poste: ['', [Validators.required]],
      dateEmbauche: ['', [Validators.required]],
      salaire: [0, [Validators.required, Validators.min(0)]], // Add the form control
      adresse: [''],
      statut: ['ACTIF', [Validators.required]]
    });
  }

private checkMode(): void {
  const id = this.route.snapshot.paramMap.get('id');
  const url = this.route.snapshot.url.map(segment => segment.path);

  if (id && !isNaN(+id)) {
    this.employeId = +id;
    this.isLoading = true;

    if (url.includes('edit')) {
      this.isEditMode = true;
    } else if (url.includes('details')) {
      this.isDetailsMode = true;
    }

    this.employeService.getEmployeById(this.employeId).subscribe({
      next: (employe) => {
        const formData = {
          ...employe,
          dateEmbauche: employe.dateEmbauche
            ? new Date(employe.dateEmbauche).toISOString().split('T')[0]
            : ''
        };

        this.employeForm.patchValue(formData);
        this.isLoading = false;

        if (this.isDetailsMode) {
          this.employeForm.disable(); // lock all fields in details mode
        }
      },
      error: () => this.handleError("Erreur lors du chargement de l'employé.")
    });

  } else {
    // --- CREATE MODE ---
    this.isEditMode = false;
    this.isDetailsMode = false;
    this.isLoading = false;
  }
}


  onSubmit(): void {
    if (this.employeForm.invalid) return;
    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.employeService.updateEmploye(this.employeId!, this.employeForm.value)
      : this.employeService.createEmploye(this.employeForm.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/employes']),
      error: () => this.handleError('Erreur lors de la sauvegarde de l\'employé.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }

}
