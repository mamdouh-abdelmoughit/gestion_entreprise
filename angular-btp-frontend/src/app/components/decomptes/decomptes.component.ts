import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Decompte } from '../../core/models/decompte.model';
import { DecompteService } from '../../core/services/decompte.service';
import { AuthService } from '../../core/services/auth.service';
import { Page } from '../../core/models/page.model';
import { User } from '../../core/models/user.model';
import {RouterLink} from "@angular/router";
import {PaginationComponent} from "../../shared/pagination/pagination.component";

@Component({
  selector: 'app-decomptes',
  standalone: true,
  imports: [CommonModule, RouterLink, PaginationComponent],
  templateUrl: './decomptes.component.html',
  styleUrls: ['./decomptes.component.css']
})
export class DecomptesComponent implements OnInit {
  decomptesPage: Page<Decompte> | null = null;
  isLoading = true;
  error: string | null = null;
  currentUser: User | null = null;

  constructor(
    private decompteService: DecompteService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
    this.loadDecomptes();
  }

  isAdmin(): boolean {
    return this.currentUser?.roles?.includes('ROLE_ADMIN') ?? false;
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
  onPageChange(newPage:number){
    this.loadDecomptes(newPage);
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
