import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import SockJS from 'sockjs-client';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

export interface ChatMessage {
  id?: number;
  contenu: string;
  expediteurId?: number;
  expediteurUsername?: string;
  expediteurNomComplet?: string;
  roleDisplay?: string;
  projetId?: number;
  projetNom?: string;
  timestamp?: string;
}

@Injectable({ providedIn: 'root' })
export class MessagerieService implements OnDestroy {
  private apiUrl = `${environment.apiUrl}/messages`;
  private rxStomp: RxStomp | null = null;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getHistory(projetId: number, limit = 100): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.apiUrl}/projet/${projetId}?limit=${limit}`);
  }

  connect(projetId: number): Observable<ChatMessage> {
    const token = this.authService.getToken();
    const subject = new Subject<ChatMessage>();

    const stompConfig: RxStompConfig = {
      webSocketFactory: () => new SockJS(`${environment.wsUrl}/ws`),
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 5000
    };

    this.rxStomp = new RxStomp();
    this.rxStomp.configure(stompConfig);
    this.rxStomp.activate();

    this.rxStomp.watch(`/topic/projet/${projetId}`).subscribe(frame => {
      const msg: ChatMessage = JSON.parse(frame.body);
      subject.next(msg);
    });

    return subject.asObservable();
  }

  send(projetId: number, contenu: string): void {
    if (!this.rxStomp) return;
    this.rxStomp.publish({
      destination: `/app/projet/${projetId}/send`,
      body: JSON.stringify({ contenu, projetId })
    });
  }

  disconnect(): void {
    if (this.rxStomp) {
      this.rxStomp.deactivate();
      this.rxStomp = null;
    }
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
