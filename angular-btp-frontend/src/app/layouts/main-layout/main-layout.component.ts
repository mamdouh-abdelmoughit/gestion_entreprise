import { Component, OnInit } from '@angular/core'; // Import OnInit
import { CommonModule } from '@angular/common';
import {Router, RouterOutlet, NavigationEnd, GuardsCheckEnd} from '@angular/router'; // Import Router and NavigationEnd
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { AuthService } from '../../core/services/auth.service'; // Import AuthService
import { User } from '../../core/models/user.model';

import { filter } from 'rxjs/operators'; // Import filter

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, HeaderComponent],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent implements OnInit {
  userEmail: string | null = null;
  activeModule: string = 'dashboard';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // FIX: Add the 'User | null' type
    this.authService.currentUser$.subscribe((user: User | null) => {
      this.userEmail = user ? user.email : 'Utilisateur';
    });

    // Listen to route changes to update the active module in the sidebar
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      // Because of the filter above, we can now be 100% sure that 'event' is a NavigationEnd event.
      // The compiler will no longer complain.
      const currentModule = event.urlAfterRedirects.split('/')[1] || 'dashboard';
      this.activeModule = currentModule;
    });
  }

  // This method will handle navigation when a sidebar item is clicked
  onModuleChange(moduleId: string): void {
    this.router.navigate([`/${moduleId}`]);
  }
}
