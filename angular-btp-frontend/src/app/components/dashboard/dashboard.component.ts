import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProjetService } from '../../core/services/projet.service';
import { AppelOffreService } from '../../core/services/appel-offre.service';
import { EmployeService } from '../../core/services/employe.service';
import { ClientService } from '../../core/services/client.service';
import { AuthService } from '../../core/services/auth.service';
import { Page } from '../../core/models/page.model';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats = {
    projetsEnCours: 0,
    appelOffres: 0,
    employes: 0,
    clients: 0
  };

  loading = true;
  currentUser: User | null = null;

  constructor(
    private projetService: ProjetService,
    private appelOffreService: AppelOffreService,
    private employeService: EmployeService,
    private clientService: ClientService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      // Only load data when we have a valid user with roles
      if (user && user.roles && user.roles.length > 0) {
        this.loadDashboardData();
      }
    });
  }

  // Role check helpers
  isAdmin(): boolean {
    return this.currentUser?.roles?.includes('ROLE_ADMIN') ?? false;
  }

  isEmployee(): boolean {
    return this.currentUser?.roles?.includes('ROLE_EMPLOYEE') ?? false;
  }

  isClient(): boolean {
    return this.currentUser?.roles?.includes('ROLE_CLIENT') ?? false;
  }

  isFournisseur(): boolean {
    return this.currentUser?.roles?.includes('ROLE_FOURNISSEUR') ?? false;
  }

  // Check if user has any of the specified roles
  hasAnyRole(...roles: string[]): boolean {
    if (!this.currentUser?.roles) return false;
    return roles.some(role => this.currentUser!.roles.includes(role));
  }

  loadDashboardData() {
    this.loading = true;

    // Load projets count (ADMIN, EMPLOYEE, CLIENT can see)
    if (this.hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_CLIENT')) {
      this.projetService.getAllProjets(0, 1, 'id,asc').subscribe({
        next: (response: Page<any>) => {
          this.stats.projetsEnCours = response.totalElements || 0;
        },
        error: () => this.stats.projetsEnCours = 0
      });
    }

    // Load appels d'offres count (ADMIN, FOURNISSEUR can see)
    if (this.hasAnyRole('ROLE_ADMIN', 'ROLE_FOURNISSEUR')) {
      this.appelOffreService.getAllAppelOffres(0, 1, 'id,asc').subscribe({
        next: (response: Page<any>) => {
          this.stats.appelOffres = response.totalElements || 0;
        },
        error: () => this.stats.appelOffres = 0
      });
    }

    // Load employes count (ADMIN only)
    if (this.isAdmin()) {
      this.employeService.getAllEmployes(0, 1, 'id,asc').subscribe({
        next: (response: Page<any>) => {
          this.stats.employes = response.totalElements || 0;
        },
        error: () => this.stats.employes = 0
      });
    }

    // Load clients count (ADMIN only)
    if (this.isAdmin()) {
      this.clientService.getAllClients(0, 1, 'id,asc').subscribe({
        next: (response: Page<any>) => {
          this.stats.clients = response.totalElements || 0;
          this.loading = false;
        },
        error: () => {
          this.stats.clients = 0;
          this.loading = false;
        }
      });
    } else {
      this.loading = false;
    }
  }
}
