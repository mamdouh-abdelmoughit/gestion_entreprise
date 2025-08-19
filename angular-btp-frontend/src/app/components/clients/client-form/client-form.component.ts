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
  isDetailsMode = false;
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
    this.checkMode();
  
  }

  private initForm(): void {
    this.clientForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: [''],
      adresse: ['']
    });
  }

private checkMode(): void {
  this.route.url.subscribe(segments => {
    if (segments.some(s => s.path === 'edit')) {
      this.isEditMode = true;
    } else if (segments.some(s => s.path === 'details')) {
      this.isDetailsMode = true;
    }
  });

  this.route.params.subscribe(params => {
    const id = params['id'];
    if (id) {
      this.clientId = +id;
      this.isLoading = true;
      this.clientService.getClientById(this.clientId).subscribe({
        next: (client) => {
          this.clientForm.patchValue(client);
          this.isLoading = false;

          // Disable form in details mode
          if (this.isDetailsMode) {
            this.clientForm.disable();
          }
        },
        error: () => {
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
