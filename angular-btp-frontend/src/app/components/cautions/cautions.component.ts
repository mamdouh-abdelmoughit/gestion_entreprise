import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Caution } from '../../core/models/caution.model';
import { CautionService } from '../../core/services/caution.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-cautions',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cautions.component.html',
  styleUrls: ['./cautions.component.css']
})

export class CautionsComponent implements OnInit {
  cautionsPage: Page<Caution> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private cautionService: CautionService) {}

  ngOnInit(): void {
    this.loadCautions();
  }

  loadCautions(page = 0, size = 10, sort = 'dateEcheance,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.cautionService.getAllCautions(page, size, sort).subscribe({
      next: (data) => {
        this.cautionsPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des cautions.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  deleteCaution(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette caution ?')) {
      this.cautionService.deleteCaution(id).subscribe({
        next: () => {
          console.log('Caution supprimée');
          this.loadCautions(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression de la caution.';
          console.error(err);
        }
      });
    }
  }
}

