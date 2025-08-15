import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { User } from '../../../core/models/user.model';
import { UserService } from '../../../core/services/user.service';
import { Page } from '../../../core/models/page.model';

@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './users-list.component.html'
})
export class UsersListComponent implements OnInit {
  usersPage: Page<User> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(page = 0, size = 10, sort = 'username,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.userService.getAll(page, size, sort).subscribe({
      next: (data) => {
        this.usersPage = data;
        this.isLoading = false;
      },
      error: () => this.handleError('Erreur de chargement des utilisateurs.')
    });
  }

  deleteUser(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      this.userService.delete(id).subscribe({
        next: () => this.loadUsers(),
        error: () => this.handleError('Erreur de suppression.')
      });
    }
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
