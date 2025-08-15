import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Client } from '../../core/models/client.model';
import { ClientService } from '../../core/services/client.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  clientsPage: Page<Client> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(page = 0, size = 10, sort = 'nom,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.clientService.getAllClients(page, size, sort).subscribe({
      next: (data) => {
        this.clientsPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des clients.';
        this.isLoading = false;
        console.error(err);
      }
    });
  } /**
   * Deletes a client after user confirmation.
   * @param id The ID of the client to delete.
   */
  deleteClient(id: number): void {
    // 1. Ask for confirmation before proceeding.
    if (confirm('Êtes-vous sûr de vouloir supprimer ce client ?')) {
      this.clientService.deleteClient(id).subscribe({
        next: () => {
          // 2. On success, show a confirmation and reload the data.
          console.log('Client supprimé avec succès');
          this.loadClients(); // Refresh the list
        },
        error: (err) => {
          // 3. On failure, display an error message.
          this.error = 'Erreur lors de la suppression du client.';
          console.error(err);
        }
      });
    }
  }
  // --- END OF NEW CODE ---
}
