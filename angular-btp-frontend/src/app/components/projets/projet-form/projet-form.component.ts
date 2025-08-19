import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjetService } from '../../../core/services/projet.service';
import { ClientService } from '../../../core/services/client.service';
import { Client } from '../../../core/models/client.model';
import {Projet} from "../../../core/models/projet.model";
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
  isDetailsMode = false;
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
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadDropdownData();
    this.checkMode();
  }

  private initForm(data?: Projet): void {
    this.projetForm = this.fb.group({
      numero: [data?.numero || '', [Validators.required]], // FIX: Add FormControl
      nom: [data?.nom || '', [Validators.required]],
      maitreDOuvrage: [data?.maitreDOuvrage || '', [Validators.required]], // FIX: Add FormControl
      description: [data?.description || ''],
      dateDebut: [data ? new Date(data.dateDebut).toISOString().split('T')[0] : '', [Validators.required]],
      dateFin: [data ? new Date(data.dateFin).toISOString().split('T')[0] : '', [Validators.required]],
      montantContrat: [data?.montantContrat ?? 0, [Validators.required, Validators.min(0)]], // FIX: Rename FormControl
      adresse: [data?.adresse || ''],
      statut: [data?.statut || 'EN_PREPARATION', [Validators.required]],
      clientId: [data?.clientId || null, [Validators.required]],
      chefProjetId: [data?.chefProjetId || null, [Validators.required]]
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

private checkMode(): void {
  const id = this.route.snapshot.paramMap.get('id');
  const url = this.route.snapshot.url.map(segment => segment.path);

  if (id && !isNaN(+id)) {
    this.projetId = +id;
    this.isLoading = true;

    if (url.includes('edit')) {
      this.isEditMode = true;
    } else if (url.includes('details')) {
      this.isDetailsMode = true;
    }

    this.projetService.getProjetById(this.projetId).subscribe({
      next: (projet) => {
        const formattedProjet = {
          ...projet,
          dateDebut: projet.dateDebut ? new Date(projet.dateDebut).toISOString().split('T')[0] : '',
          dateFin: projet.dateFin ? new Date(projet.dateFin).toISOString().split('T')[0] : ''
        };

        this.projetForm.patchValue(formattedProjet);
        this.isLoading = false;

        // Disable form if in details mode
        if (this.isDetailsMode) {
          this.projetForm.disable();
        }
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement du projet.';
        this.isLoading = false;
      }
    });

  } else {
    // --- CREATE MODE ---
    this.isEditMode = false;
    this.isDetailsMode = false;
    this.isLoading = false;
  }
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
  loadProjetDetails(id: number): void {
    this.isLoading = true;
    this.isDetailsMode = true; // Assuming this is set to true for details view
    this.error = null;
    this.projetService.getProjetById(id).subscribe({
      next: (projet) => {
        this.initForm(projet);
        this.isLoading = false;
        if (this.isDetailsMode) {
          this.projetForm.disable();
        }
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement du projet.';
        this.isLoading = false;
      }
    });
  }
}
