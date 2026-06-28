import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Depense } from '../../core/models/depense.model';
import { DepenseService } from '../../core/services/depense.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";
import {PaginationComponent} from "../../shared/pagination/pagination.component";
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-depenses',
  standalone: true,
  imports: [CommonModule, RouterLink, PaginationComponent],
  templateUrl: './depenses.component.html',
  styleUrls: ['./depenses.component.css']
})
export class DepensesComponent implements OnInit {
  depensesPage: Page<Depense> | null = null;
  isLoading = true;
  error: string | null = null;
  currentUser: User | null = null;

  constructor(
    private depenseService: DepenseService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => this.currentUser = user);
    this.loadDepenses();
  }

  isAdmin(): boolean {
    return this.currentUser?.roles?.includes('ROLE_ADMIN') ?? false;
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
  onPageChange(newPage:number){
    this.loadDepenses(newPage);
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
