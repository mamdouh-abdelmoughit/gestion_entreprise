import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AffectationEmploye } from '../../../core/models/affectation-employe.model';
import { AffectationEmployeService } from '../../../core/services/affectation-employe.service';
import { Page } from '../../../core/models/page.model';

@Component({
  selector: 'app-affectations-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './affectations-list.component.html'
})
export class AffectationsListComponent implements OnInit {
  affectationsPage: Page<AffectationEmploye> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private affectationService: AffectationEmployeService) {}

  ngOnInit(): void {
    this.loadAffectations();
  }

  loadAffectations(page = 0, size = 10, sort = 'dateDebut,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.affectationService.getAll(page, size, sort).subscribe({
      next: (data) => {
        this.affectationsPage = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Erreur lors du chargement des affectations.';
        this.isLoading = false;
      }
    });
  }

  deleteAffectation(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette affectation ?')) {
      this.affectationService.delete(id).subscribe({
        next: () => this.loadAffectations(),
        error: () => this.error = 'Erreur de suppression.'
      });
    }
  }
}
