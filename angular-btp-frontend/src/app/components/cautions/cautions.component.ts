import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Caution } from '../../core/models/caution.model';
import { CautionService } from '../../core/services/caution.service';
import { AuthService } from '../../core/services/auth.service';
import { Page } from '../../core/models/page.model';
import { User } from '../../core/models/user.model';
import {RouterLink} from "@angular/router";
import {PaginationComponent} from "../../shared/pagination/pagination.component";

@Component({
  selector: 'app-cautions',
  standalone: true,
  imports: [CommonModule, RouterLink, PaginationComponent],
  templateUrl: './cautions.component.html',
  styleUrls: ['./cautions.component.css']
})

export class CautionsComponent implements OnInit {
  cautionsPage: Page<Caution> | null = null;
  isLoading = true;
  error: string | null = null;
  currentUser: User | null = null;

  constructor(
    private cautionService: CautionService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
    this.loadCautions();
  }

  isAdmin(): boolean {
    return this.currentUser?.roles?.includes('ROLE_ADMIN') ?? false;
  }

  loadCautions(page = 0, size = 10, sort = 'dateExpiration,asc'): void {
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
  onPageChange(newPage:number){
    this.loadCautions(newPage);
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

