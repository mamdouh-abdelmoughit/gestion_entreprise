import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../core/services/user.service';

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
  isLoading = true; // Start as true to load user data

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    protected router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.checkEditMode();
  }

  private initForm(): void {
    this.form = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      enabled: [true],
      // Roles are often managed separately, but for a simple form we can use a text input
      roles: ['']
    });
  }

  private checkEditMode(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      if (!this.userId) {
        // Should not happen as we don't have a 'new' user route
        this.router.navigate(['/users']);
        return;
      }

      this.userService.getById(this.userId).subscribe({
        next: (user) => {
          const formData = { ...user, roles: user.roles.join(', ') };
          this.form.patchValue(formData);
          this.isLoading = false;
        },
        error: () => this.handleError('Erreur de chargement de l\'utilisateur.')
      });
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.isLoading = true;
    this.error = null;

    const formValue = this.form.value;
    const dataToSend = {
      ...formValue,
      roles: formValue.roles.split(',').map((s: string) => s.trim()).filter((s: string) => s)
    };

    this.userService.update(this.userId!, dataToSend).subscribe({
      next: () => this.router.navigate(['/users']),
      error: () => this.handleError('Erreur de sauvegarde.')
    });
  }

  private handleError(message: string) {
    this.error = message;
    this.isLoading = false;
  }
}
