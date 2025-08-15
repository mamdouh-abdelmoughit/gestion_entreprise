import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../../../core/services/client.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.css']
})
export class ClientFormComponent implements OnInit {
  clientForm!: FormGroup;
  isEditMode = false;
  clientId: number | null = null;
  error: string | null = null;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.checkEditMode();
  }

  private initForm(): void {
    this.clientForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      adresse: ['']
    });
  }

  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.isEditMode = true;
        this.clientId = +id; // The '+' converts the string to a number
        this.isLoading = true;
        this.clientService.getClientById(this.clientId).subscribe({
          next: (client) => {
            this.clientForm.patchValue(client); // Pre-fill the form with existing data
            this.isLoading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors du chargement du client.';
            this.isLoading = false;
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.clientForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.error = null;
    const clientData: Partial<Client> = this.clientForm.value;

    const saveOperation = this.isEditMode
      ? this.clientService.updateClient(this.clientId!, clientData)
      : this.clientService.createClient(clientData);

    saveOperation.subscribe({
      next: () => {
        this.router.navigate(['/clients']); // Navigate back to the list on success
      },
      error: (err) => {
        this.error = 'Erreur lors de la sauvegarde du client.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
}
