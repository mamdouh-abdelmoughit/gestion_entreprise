import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../core/services/user.service';
import { AuthService } from '../../../core/services/auth.service'; // 1. Import AuthService
import { RoleService } from '../../../core/services/role.service';
import { Role } from '../../../core/models/role.model';
import { forkJoin, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.component.html' // We will fix the HTML next
})
export class UserFormComponent implements OnInit {
  form!: FormGroup;
  userId: number | null = null;
  error: string | null = null;
  isEditMode = false;
  isLoading = true;
  availableRoles: Role[] = [];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService, // 2. Inject AuthService
    private roleService: RoleService,
    public router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!id;
    this.userId = id ? +id : null;

    this.initForm();
    this.loadData();
  }

  private initForm(): void {
    this.form = this.fb.group({
      // In create mode, password is not needed. It will be hidden in the HTML.
      // In edit mode, we might want to change it, but for now we simplify.
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      firstName: [''],
      lastName: [''],
      enabled: [true],
      roles: [[], [Validators.required]] // Roles are required
    });
  }

  private loadData(): void {
    const rolesObservable = this.roleService.getAll(0, 100, 'nom,asc').pipe(map(page => page.content));

    if (this.isEditMode && this.userId) {
      const userObservable = this.userService.getById(this.userId);
      forkJoin([rolesObservable, userObservable]).subscribe(([roles, user]) => {
        this.availableRoles = roles;
        this.form.patchValue({ ...user, roles: user.roles });
        this.isLoading = false;
      });
    } else {
      rolesObservable.subscribe(roles => {
        this.availableRoles = roles;
        this.isLoading = false;
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.isLoading = true;
    this.error = null;

    const formValue = this.form.value;

    if (this.isEditMode && this.userId) {
      // Logic for updating a user (keeps existing logic)
      this.userService.update(this.userId, formValue).subscribe({
        next: () => this.router.navigate(['/users']),
        error: (err) => this.handleError(err.error?.message || 'Erreur de sauvegarde.')
      });
    } else {
      // --- THIS IS THE CRITICAL FIX ---
      // Logic for creating a new user via invitation
      this.authService.inviteUser(formValue.email, formValue.username, formValue.roles).subscribe({
        next: () => this.router.navigate(['/users']),
        error: (err) => this.handleError(err.error?.message || 'Erreur lors de l\'invitation.')
      });
    }
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
