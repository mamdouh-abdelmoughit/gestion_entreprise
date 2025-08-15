import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Depense } from '../../core/models/depense.model';
import { DepenseService } from '../../core/services/depense.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-depenses',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './depenses.component.html',
  styleUrls: ['./depenses.component.css']
})
export class DepensesComponent implements OnInit {
  depensesPage: Page<Depense> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private depenseService: DepenseService) {}

  ngOnInit(): void {
    this.loadDepenses();
  }

  loadDepenses(page = 0, size = 10, sort = 'dateDepense,desc'): void {
    this.isLoading = true;
    this.error = null;
    this.depenseService.getAllDepenses(page, size, sort).subscribe({
      next: (data) => {
        this.depensesPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des dépenses.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  deleteDepense(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette dépense ?')) {
      this.depenseService.deleteDepense(id).subscribe({
        next: () => {
          console.log('Dépense supprimée');
          this.loadDepenses(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression de la dépense.';
          console.error(err);
        }
      });
    }
  }
}
