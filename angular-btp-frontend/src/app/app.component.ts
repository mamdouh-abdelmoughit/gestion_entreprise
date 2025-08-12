import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, HeaderComponent],
  template: `
    <div class="min-h-screen flex bg-gray-50">
      <app-sidebar (moduleChange)="onModuleChange($event)"></app-sidebar>
      <div class="flex-1 flex flex-col">
        <app-header [userEmail]="userEmail"></app-header>
        <main class="flex-1 p-6 overflow-auto">
          <router-outlet></router-outlet>
        </main>
      </div>
    </div>
  `,
  styles: []
})
export class AppComponent {
  userEmail = 'admin@btp.com';
  activeModule = 'dashboard';

  onModuleChange(module: string) {
    this.activeModule = module;
  }
}
