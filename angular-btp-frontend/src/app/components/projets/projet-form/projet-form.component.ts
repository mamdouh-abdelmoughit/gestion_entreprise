import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjetService } from '../../../core/services/projet.service';
import { ClientService } from '../../../core/services/client.service';
import { Client } from '../../../core/models/client.model';
// We need a simple User model for the dropdown
interface UserRef { id: number; username: string; }

@Component({
  selector: 'app-projet-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './projet-form.component.html',
  styleUrls: ['./projet-form.component.css']
})
export class ProjetFormComponent implements OnInit {
  projetForm!: FormGroup;
  isEditMode = false;
  projetId: number | null = null;
  error: string | null = null;
  isLoading = false;

  // Data for dropdowns
  clients: Client[] = [];
  chefsProjet: UserRef[] = []; // Assuming a simple user structure for now

  constructor(
    private fb: FormBuilder,
    private projetService: ProjetService,
    private clientService: ClientService,
    // private userService: UserService, // You will create this service later
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadDropdownData();
    this.checkEditMode();
  }

  private initForm(): void {
    this.projetForm = this.fb.group({
      nom: ['', [Validators.required]],
      description: [''],
      dateDebut: ['', [Validators.required]],
      dateFin: ['', [Validators.required]],
      budget: [0, [Validators.required, Validators.min(0)]],
      adresse: [''],
      statut: ['EN_PREPARATION', [Validators.required]],
      clientId: [null, [Validators.required]],
      chefProjetId: [null, [Validators.required]]
    });
  }

  private loadDropdownData(): void {
    // Load clients for the client dropdown
    this.clientService.getAllClients(0, 100, 'nom,asc').subscribe(page => this.clients = page.content);
    // TODO: When UserService is created, load users here. For now, we'll use a placeholder.
    this.chefsProjet = [
      { id: 1, username: 'projectmanager' } // Placeholder for the user we created
    ];
  }

  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEditMode = true;
        this.projetId = +id;
        this.isLoading = true;
        this.projetService.getProjetById(this.projetId).subscribe({
          next: (projet) => {
            // Dates need to be formatted for the date input field (YYYY-MM-DD)
            const formattedProjet = {
              ...projet,
              dateDebut: projet.dateDebut ? new Date(projet.dateDebut).toISOString().split('T')[0] : '',
              dateFin: projet.dateFin ? new Date(projet.dateFin).toISOString().split('T')[0] : ''
            };
            this.projetForm.patchValue(formattedProjet);
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors du chargement du projet.';
            this.isLoading = false;
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.projetForm.invalid) {
      return;
    }
    this.isLoading = true;
    this.error = null;

    const saveOperation = this.isEditMode
      ? this.projetService.updateProjet(this.projetId!, this.projetForm.value)
      : this.projetService.createProjet(this.projetForm.value);

    saveOperation.subscribe({
      next: () => this.router.navigate(['/projets']),
      error: (err) => {
        this.error = 'Erreur lors de la sauvegarde du projet.';
        this.isLoading = false;
      }
    });
  }
}
