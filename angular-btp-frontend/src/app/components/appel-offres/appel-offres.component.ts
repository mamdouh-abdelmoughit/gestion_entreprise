import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppelOffre } from '../../core/models/appel-offre.model';
import { AppelOffreService } from '../../core/services/appel-offre.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-appel-offres',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './appel-offres.component.html',
  styleUrls: ['./appel-offres.component.css']
})
export class AppelOffresComponent implements OnInit {
  appelOffresPage: Page<AppelOffre> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private appelOffreService: AppelOffreService) {}

  ngOnInit(): void {
    this.loadAppelOffres();
  }

  loadAppelOffres(page = 0, size = 10, sort = 'dateLimite,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.appelOffreService.getAllAppelOffres(page, size, sort).subscribe({
      next: (data) => {
        this.appelOffresPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = "Erreur lors du chargement des appels d'offres.";
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  deleteAppelOffre(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet appel d\'offres ?')) {
      this.appelOffreService.deleteAppelOffre(id).subscribe({
        next: () => {
          console.log('Appel d\'offres supprimé');
          this.loadAppelOffres(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression de l\'appel d\'offres.';
          console.error(err);
        }
      });
    }
  }
}

