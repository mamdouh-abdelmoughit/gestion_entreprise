import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Decompte } from '../../core/models/decompte.model';
import { DecompteService } from '../../core/services/decompte.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-decomptes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './decomptes.component.html',
  styleUrls: ['./decomptes.component.css']
})
export class DecomptesComponent implements OnInit {
  decomptesPage: Page<Decompte> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private decompteService: DecompteService) {}

  ngOnInit(): void {
    this.loadDecomptes();
  }

  loadDecomptes(page = 0, size = 10, sort = 'dateDecompte,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.decompteService.getAllDecomptes(page, size, sort).subscribe({
      next: (data) => {
        this.decomptesPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des décomptes.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  deleteDecompte(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce décompte ?')) {
      this.decompteService.deleteDecompte(id).subscribe({
        next: () => {
          console.log('Décompte supprimé');
          this.loadDecomptes(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression du décompte.';
          console.error(err);
        }
      });
    }
  }
}
