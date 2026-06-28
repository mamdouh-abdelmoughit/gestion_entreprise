import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, HeaderComponent],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent implements OnInit {
  // --- START CHANGE ---
  // userEmail: string | null = null; // DELETE THIS LINE
  currentUser: User | null = null; // ADD THIS LINE
  // --- END CHANGE ---
  activeModule: string = 'dashboard';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // --- START CHANGE ---
    this.authService.currentUser$.subscribe((user: User | null) => {
      // this.userEmail = user ? user.email : 'Utilisateur'; // DELETE THIS LINE
      this.currentUser = user; // ADD THIS LINE
    });
    // --- END CHANGE ---

    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      const currentModule = event.urlAfterRedirects.split('/')[1] || 'dashboard';
      this.activeModule = currentModule;
    });
  }

  onModuleChange(moduleId: string): void {
    this.router.navigate([`/${moduleId}`]);
  }
}
