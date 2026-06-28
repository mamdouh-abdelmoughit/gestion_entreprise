import { Component, OnInit, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Observable } from 'rxjs';
import { SwUpdate, VersionReadyEvent } from '@angular/service-worker';
import { filter } from 'rxjs/operators';
import { AuthService } from './core/services/auth.service';
import { ToastComponent } from "./shared/toast/toast.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ToastComponent],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'angular-btp-frontend';
  isLoggedIn$: Observable<boolean>;
  userEmail = 'admin@btp.com';
  activeModule = 'dashboard';

  constructor(
    private authService: AuthService,
    @Optional() private swUpdate: SwUpdate
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  ngOnInit() {
    if (this.swUpdate?.isEnabled) {
      this.swUpdate.versionUpdates.pipe(
        filter((evt): evt is VersionReadyEvent => evt.type === 'VERSION_READY')
      ).subscribe(() => {
        this.swUpdate.activateUpdate().then(() => window.location.reload());
      });
    }
  }

  onModuleChange(module: string) {
    this.activeModule = module;
  }
}
