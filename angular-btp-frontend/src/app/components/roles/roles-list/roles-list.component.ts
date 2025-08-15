import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Role } from '../../../core/models/role.model';
import { RoleService } from '../../../core/services/role.service';
import { Page } from '../../../core/models/page.model';

@Component({
  selector: 'app-roles-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './roles-list.component.html'
})
export class RolesListComponent implements OnInit {
  rolesPage: Page<Role> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private roleService: RoleService) {}

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles(page = 0, size = 10, sort = 'nom,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.roleService.getAll(page, size, sort).subscribe({
      next: (data) => {
        this.rolesPage = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Erreur de chargement des rôles.';
        this.isLoading = false;
      }
    });
  }
}
