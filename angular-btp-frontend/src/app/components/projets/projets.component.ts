import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Projet } from '../../core/models/projet.model';
import { ProjetService } from '../../core/services/projet.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-projets',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './projets.component.html',
  styleUrls: ['./projets.component.css']
})
export class ProjetsComponent implements OnInit {
  projetsPage: Page<Projet> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private projetService: ProjetService) {}

  ngOnInit(): void {
    this.loadProjets();
  }

  loadProjets(page = 0, size = 10, sort = 'dateDebut,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.projetService.getAllProjets(page, size, sort).subscribe({
      next: (data) => {
        this.projetsPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des projets.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }deleteProjet(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce projet ? Cette action est irréversible.')) {
      this.projetService.deleteProjet(id).subscribe({
        next: () => {
          console.log('Projet supprimé');
          this.loadProjets(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression du projet.';
          console.error(err);
        }
      });
    }
  }
}
