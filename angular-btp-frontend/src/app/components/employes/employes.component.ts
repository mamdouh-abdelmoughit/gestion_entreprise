import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Employe } from '../../core/models/employe.model';
import { EmployeService } from '../../core/services/employe.service';
import { Page } from '../../core/models/page.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-employes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './employes.component.html',
  styleUrls: ['./employes.component.css']
})
export class EmployesComponent implements OnInit {
  employesPage: Page<Employe> | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(private employeService: EmployeService) {}

  ngOnInit(): void {
    this.loadEmployes();
  }

  loadEmployes(page = 0, size = 10, sort = 'nom,asc'): void {
    this.isLoading = true;
    this.error = null;
    this.employeService.getAllEmployes(page, size, sort).subscribe({
      next: (data) => {
        this.employesPage = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des employés.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }
  deleteEmploye(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet employé ?')) {
      this.employeService.deleteEmploye(id).subscribe({
        next: () => {
          console.log('Employé supprimé');
          this.loadEmployes(); // Refresh the list
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression de l\'employé.';
          console.error(err);
        }
      });
    }
  }
}

