import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Toast {
  message: string;
  type: 'success' | 'error';
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private toastSubject = new BehaviorSubject<Toast | null>(null);
  toastState$: Observable<Toast | null> = this.toastSubject.asObservable();

  constructor() { }

  show(message: string, type: 'success' | 'error' = 'success') {
    this.toastSubject.next({ message, type });
    // Automatically hide the toast after 5 seconds
    setTimeout(() => this.hide(), 5000);
  }

  hide() {
    this.toastSubject.next(null);
  }
}
