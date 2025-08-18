import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { NotificationService, Toast } from '../../core/services/notification.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html'
})
export class ToastComponent {
  toast$: Observable<Toast | null>;

  constructor(private notificationService: NotificationService) {
    this.toast$ = this.notificationService.toastState$;
  }

  close() {
    this.notificationService.hide();
  }
}
