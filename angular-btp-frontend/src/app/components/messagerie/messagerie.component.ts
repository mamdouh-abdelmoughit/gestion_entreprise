import { Component, OnInit, OnDestroy, AfterViewChecked, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { MessagerieService, ChatMessage } from '../../core/services/messagerie.service';
import { AuthService } from '../../core/services/auth.service';
import { ProjetService } from '../../core/services/projet.service';

@Component({
  selector: 'app-messagerie',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="flex h-full" style="height: calc(100vh - 64px);">

      <!-- Projet selector sidebar -->
      <div class="w-72 border-r border-gray-200 bg-white flex flex-col">
        <div class="px-4 py-4 border-b border-gray-100">
          <h2 class="font-semibold text-secondary-800 text-sm">Messagerie projets</h2>
          <p class="text-xs text-secondary-400 mt-0.5">Sélectionnez un projet</p>
        </div>
        <div class="flex-1 overflow-y-auto">
          <button *ngFor="let p of projets"
            (click)="selectProjet(p)"
            [class]="selectedProjet?.id === p.id
              ? 'w-full text-left px-4 py-3 border-l-2 border-primary-500 bg-primary-50'
              : 'w-full text-left px-4 py-3 border-l-2 border-transparent hover:bg-gray-50'">
            <div class="text-sm font-medium text-secondary-800">{{ p.nom }}</div>
            <div class="text-xs text-secondary-400 mt-0.5">{{ p.reference || '—' }}</div>
          </button>
          <div *ngIf="projets.length === 0" class="px-4 py-8 text-center text-secondary-400 text-sm">
            Aucun projet disponible
          </div>
        </div>
      </div>

      <!-- Chat area -->
      <div class="flex-1 flex flex-col bg-gray-50">
        <div *ngIf="!selectedProjet" class="flex-1 flex items-center justify-center text-secondary-400">
          <div class="text-center">
            <svg class="w-12 h-12 mx-auto mb-3 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
            <p class="text-sm">Sélectionnez un projet pour démarrer la discussion</p>
          </div>
        </div>

        <ng-container *ngIf="selectedProjet">
          <!-- Chat header -->
          <div class="px-6 py-4 bg-white border-b border-gray-200 flex items-center justify-between">
            <div>
              <h3 class="font-semibold text-secondary-800">{{ selectedProjet.nom }}</h3>
              <span *ngIf="connected" class="inline-flex items-center gap-1 text-xs text-green-600">
                <span class="w-1.5 h-1.5 bg-green-500 rounded-full"></span> Connecté
              </span>
              <span *ngIf="!connected" class="inline-flex items-center gap-1 text-xs text-gray-400">
                <span class="w-1.5 h-1.5 bg-gray-400 rounded-full"></span> Déconnecté
              </span>
            </div>
          </div>

          <!-- Messages area -->
          <div #messagesContainer class="flex-1 overflow-y-auto p-6 space-y-3">
            <div *ngIf="loadingHistory" class="text-center text-secondary-400 text-sm py-4">Chargement...</div>

            <div *ngFor="let msg of messages"
              [class]="isMine(msg) ? 'flex justify-end' : 'flex justify-start'">
              <div [class]="isMine(msg) ? 'max-w-xs lg:max-w-md' : 'max-w-xs lg:max-w-md'">
                <div *ngIf="!isMine(msg)" class="flex items-center gap-1 mb-1">
                  <span class="text-xs font-medium text-secondary-700">{{ msg.expediteurNomComplet || msg.expediteurUsername }}</span>
                  <span class="text-xs text-secondary-400">({{ msg.roleDisplay }})</span>
                </div>
                <div [class]="isMine(msg)
                  ? 'bg-primary-600 text-white rounded-2xl rounded-tr-sm px-4 py-2.5 text-sm'
                  : 'bg-white border border-gray-200 text-secondary-800 rounded-2xl rounded-tl-sm px-4 py-2.5 text-sm shadow-sm'">
                  {{ msg.contenu }}
                </div>
                <div [class]="isMine(msg) ? 'text-right' : 'text-left'" class="mt-1">
                  <span class="text-xs text-secondary-400">{{ formatTime(msg.timestamp) }}</span>
                </div>
              </div>
            </div>

            <div #bottomAnchor></div>
          </div>

          <!-- Input area -->
          <div class="p-4 bg-white border-t border-gray-200">
            <form (ngSubmit)="sendMessage()" class="flex gap-3 items-end">
              <textarea [formControl]="messageInput"
                rows="2"
                placeholder="Saisir votre message... (Entrée pour envoyer)"
                (keydown.enter)="onEnter($event)"
                class="flex-1 border border-gray-300 rounded-xl px-4 py-2.5 text-sm resize-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500">
              </textarea>
              <button type="submit"
                [disabled]="!messageInput.value?.trim() || !connected"
                class="bg-primary-600 text-white p-2.5 rounded-xl hover:bg-primary-700 disabled:opacity-40 flex-shrink-0">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"/>
                </svg>
              </button>
            </form>
          </div>
        </ng-container>
      </div>
    </div>
  `
})
export class MessagerieComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('bottomAnchor') bottomAnchor!: ElementRef;

  projets: any[] = [];
  selectedProjet: any = null;
  messages: ChatMessage[] = [];
  connected = false;
  loadingHistory = false;
  messageInput = new FormControl('', Validators.required);

  private sub?: Subscription;
  private shouldScroll = false;
  private currentUserId?: number;

  constructor(
    private messagerieService: MessagerieService,
    private projetService: ProjetService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => { this.currentUserId = user?.id; });
    this.projetService.getAllProjets(0, 200, 'nom').subscribe(p => {
      this.projets = p.content;
      const pid = this.route.snapshot.queryParamMap.get('projetId');
      if (pid) {
        const found = this.projets.find(x => x.id === +pid);
        if (found) this.selectProjet(found);
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.messagerieService.disconnect();
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollBottom();
      this.shouldScroll = false;
    }
  }

  selectProjet(p: any): void {
    if (this.selectedProjet?.id === p.id) return;
    this.sub?.unsubscribe();
    this.messagerieService.disconnect();
    this.selectedProjet = p;
    this.messages = [];
    this.connected = false;
    this.loadHistory(p.id);
  }

  loadHistory(projetId: number): void {
    this.loadingHistory = true;
    this.messagerieService.getHistory(projetId).subscribe({
      next: hist => {
        this.messages = hist;
        this.loadingHistory = false;
        this.shouldScroll = true;
        this.connectWs(projetId);
      },
      error: () => { this.loadingHistory = false; this.connectWs(projetId); }
    });
  }

  connectWs(projetId: number): void {
    this.sub = this.messagerieService.connect(projetId).subscribe({
      next: msg => {
        this.messages.push(msg);
        this.shouldScroll = true;
        this.connected = true;
      },
      error: () => { this.connected = false; }
    });
    setTimeout(() => { this.connected = true; }, 500);
  }

  isMine(msg: ChatMessage): boolean {
    return msg.expediteurId === this.currentUserId;
  }

  onEnter(event: Event): void {
    const ke = event as KeyboardEvent;
    if (!ke.shiftKey) { ke.preventDefault(); this.sendMessage(); }
  }

  sendMessage(): void {
    const contenu = this.messageInput.value?.trim();
    if (!contenu || !this.selectedProjet || !this.connected) return;
    this.messagerieService.send(this.selectedProjet.id, contenu);
    this.messageInput.reset();
  }

  scrollBottom(): void {
    try { this.bottomAnchor?.nativeElement?.scrollIntoView({ behavior: 'smooth' }); } catch {}
  }

  formatTime(ts?: string): string {
    if (!ts) return '';
    const d = new Date(ts);
    return d.toLocaleTimeString('fr-MA', { hour: '2-digit', minute: '2-digit' });
  }
}
