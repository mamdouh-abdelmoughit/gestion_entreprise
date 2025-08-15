import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Fournisseur } from '../../core/models/fournisseur.model';
import { FournisseurService } from '../../core/services/fournisseur.service';
import { Page } from '../../core/models/page.model';
import { RouterLink } from '@angular/router'; // 1. Import RouterLink


@Component({
  selector: 'app-fournisseurs',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './fournisseurs.component.html',
  styleUrls: ['./fournisseurs.component.css']
})
export class FournisseursComponent implements OnInit {
  fournisseursPage: Page<Fournisseur> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private fournisseurService: FournisseurService) {}

  ngOnInit(): void {
    this.loadFournisseurs();
  }

  loadFournisseurs(page = 0, size = 10, sort = 'nom,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.fournisseurService.getAllFournisseurs(page, size, sort).subscribe({
      next: (data) => {
        this.fournisseursPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des fournisseurs.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  deleteFournisseur(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce fournisseur ?')) {
      this.fournisseurService.deleteFournisseur(id).subscribe({
        next: () => {
          console.log('Fournisseur supprimé');
          this.loadFournisseurs(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression du fournisseur.';
          console.error(err);
        }
      });
    }
  }
}

