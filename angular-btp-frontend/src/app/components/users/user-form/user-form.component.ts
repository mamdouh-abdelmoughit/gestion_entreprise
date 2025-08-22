import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../core/services/user.service';
import { RoleService } from '../../../core/services/role.service'; // Import RoleService
import { Role } from '../../../core/models/role.model'; // Import Role model
import { forkJoin, of } from 'rxjs'; // Import 'of' for when there's no user to load
import { map, catchError } from 'rxjs/operators';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {
  form!: FormGroup;
  userId: number | null = null;
  error: string | null = null;
  isEditMode = false;
  isDetailsMode = false;
  isCreationMode = false; // Flag for creation mode
  isLoading = true;

  availableRoles: Role[] = []; // To store all roles fetched from the backend

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private roleService: RoleService, // Inject RoleService
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadData();
  }

  private initForm(): void {
    this.form = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      enabled: [true],
      roles: [[]] // Initialize with an empty array to hold selected role names
    });
  }

  private loadData(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.userId = id && !isNaN(+id) ? +id : null;

    // Determine mode based on URL
    const url = this.route.snapshot.url.map(segment => segment.path);
    this.isCreationMode = url.includes('new');
    this.isEditMode = url.includes('edit');
    this.isDetailsMode = url.includes('details');


    const rolesObservable = this.roleService.getAll(0, 100, 'nom,asc').pipe(
      map(page => page.content),
      catchError(err => {
        this.handleError("Erreur de chargement des rôles.");
        return of([]); // Return an empty array on error so forkJoin doesn't fail
      })
    );

    if (this.userId && !this.isCreationMode) {
      const userObservable = this.userService.getById(this.userId).pipe(
        catchError(err => {
          this.handleError("Erreur de chargement de l'utilisateur.");
          this.router.navigate(['/users']); // Redirect if user not found
          return of(null); // Return null on error
        })
      );

      forkJoin([rolesObservable, userObservable]).subscribe({
        next: ([roles, user]) => {
          this.availableRoles = roles;
          if (user) {
            this.form.patchValue({
              ...user,
              roles: user.roles
            });
          }
          this.isLoading = false;

          if (this.isDetailsMode) {
            this.form.disable(); // Lock all fields in details mode
          }
        },
        error: () => {
          // handleError already called by catchError operators
        }
      });
    } else if (this.isCreationMode) {
      // For new user creation, only load roles
      rolesObservable.subscribe({
        next: (roles) => {
          this.availableRoles = roles;
          this.isLoading = false;
        },
        error: () => {
          // handleError already called by catchError operator
        }
      });
    } else {
      // If there's an ID but it's invalid, or no mode matched, redirect
      this.router.navigate(['/users']);
    }
  }

  // Helper to check if a role is selected (for potential UI updates, not directly used by select multiple)
  isRoleSelected(roleNom: string): boolean {
    return this.form.get('roles')?.value.includes(roleNom);
  }

  // Handle changes in role selection from the multi-select dropdown
  onRoleSelectionChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedOptions = Array.from(selectElement.options)
                                .filter(option => option.selected)
                                .map(option => option.value);
    this.form.get('roles')?.setValue(selectedOptions);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.error = null;

    const formValue = this.form.value;
    const dataToSend = {
      ...formValue,
      roles: formValue.roles
    };

    if (this.isCreationMode) {
      this.userService.create(dataToSend).subscribe({
        next: () => this.router.navigate(['/users']),
        error: (err) => {
          const errorMessage = err.error?.message || 'Erreur de création de l\'utilisateur.';
          this.handleError(errorMessage);
        }
      });
    } else if (this.userId && this.isEditMode) {
      this.userService.update(this.userId, dataToSend).subscribe({
        next: () => this.router.navigate(['/users']),
        error: (err) => {
          const errorMessage = err.error?.message || 'Erreur de sauvegarde de l\'utilisateur.';
          this.handleError(errorMessage);
        }
      });
    } else {
      console.warn("Invalid form submission mode. Expected creation or edit.");
      this.handleError('Opération non supportée.');
      this.isLoading = false;
    }
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
