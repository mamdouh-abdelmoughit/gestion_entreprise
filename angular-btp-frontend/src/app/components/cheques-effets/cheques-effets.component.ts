import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ChequeEffetService, ChequeEffet } from '../../core/services/cheque-effet.service';

@Component({
  selector: 'app-cheques-effets',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="p-6">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-bold text-secondary-900">Chèques & Effets</h1>
          <p class="text-sm text-secondary-500 mt-1">Gestion des chèques et effets de commerce</p>
        </div>
        <button (click)="showForm = true; resetForm()"
          class="bg-primary-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-primary-700 flex items-center gap-2">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Nouveau
        </button>
      </div>

      <!-- Inline form panel -->
      <div *ngIf="showForm" class="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h2 class="font-semibold text-secondary-800 mb-4">{{ editId ? 'Modifier' : 'Nouveau chèque/effet' }}</h2>
        <form [formGroup]="form" (ngSubmit)="submit()" class="grid grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Type *</label>
            <select formControlName="type" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="CHEQUE">Chèque</option>
              <option value="EFFET">Effet</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Numéro *</label>
            <input formControlName="numero" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Banque *</label>
            <input formControlName="banque" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Montant (MAD) *</label>
            <input formControlName="montant" type="number" step="0.01" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Date émission *</label>
            <input formControlName="dateEmission" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Date échéance</label>
            <input formControlName="dateEcheance" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Bénéficiaire *</label>
            <input formControlName="beneficiaire" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Tireur</label>
            <input formControlName="tireur" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Statut</label>
            <select formControlName="statut" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="EN_ATTENTE">En attente</option>
              <option value="ENCAISSE">Encaissé</option>
              <option value="REJETE">Rejeté</option>
              <option value="RETOURNE">Retourné</option>
              <option value="ANNULE">Annulé</option>
            </select>
          </div>
          <div class="col-span-3">
            <label class="block text-sm font-medium text-secondary-700 mb-1">Notes</label>
            <textarea formControlName="notes" rows="2" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"></textarea>
          </div>
          <div *ngIf="error" class="col-span-3 bg-red-50 border border-red-200 rounded-lg p-3 text-red-700 text-sm">{{ error }}</div>
          <div class="col-span-3 flex justify-end gap-3">
            <button type="button" (click)="showForm = false" class="px-4 py-2 border border-gray-300 rounded-lg text-sm text-secondary-700">Annuler</button>
            <button type="submit" [disabled]="saving" class="px-5 py-2 bg-primary-600 text-white rounded-lg text-sm font-medium disabled:opacity-50">
              {{ saving ? '...' : (editId ? 'Mettre à jour' : 'Créer') }}
            </button>
          </div>
        </form>
      </div>

      <!-- List -->
      <div class="bg-white rounded-xl border border-gray-200 overflow-hidden">
        <div *ngIf="loading" class="p-12 text-center text-secondary-400">Chargement...</div>
        <table *ngIf="!loading" class="w-full text-sm">
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Type</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Numéro</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Banque</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Bénéficiaire</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Montant</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Émission</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Échéance</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Statut</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr *ngFor="let c of items" class="hover:bg-gray-50">
              <td class="px-4 py-3">
                <span [class]="c.type === 'CHEQUE' ? 'px-2 py-0.5 rounded text-xs font-medium bg-indigo-100 text-indigo-700' : 'px-2 py-0.5 rounded text-xs font-medium bg-teal-100 text-teal-700'">
                  {{ c.type }}
                </span>
              </td>
              <td class="px-4 py-3 font-medium text-secondary-900">{{ c.numero }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ c.banque }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ c.beneficiaire }}</td>
              <td class="px-4 py-3 text-right font-medium">{{ c.montant | number:'1.2-2' }} MAD</td>
              <td class="px-4 py-3 text-secondary-600">{{ c.dateEmission | date:'dd/MM/yyyy' }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ c.dateEcheance ? (c.dateEcheance | date:'dd/MM/yyyy') : '—' }}</td>
              <td class="px-4 py-3"><span [class]="statutCss(c.statut)">{{ c.statut }}</span></td>
              <td class="px-4 py-3 text-right">
                <div class="flex justify-end gap-1">
                  <button *ngIf="c.statut === 'EN_ATTENTE'" (click)="changeStatut(c.id!, 'ENCAISSE')"
                    class="text-green-600 hover:text-green-800 text-xs font-medium px-2">Encaisser</button>
                  <button *ngIf="c.statut === 'EN_ATTENTE'" (click)="changeStatut(c.id!, 'REJETE')"
                    class="text-red-500 hover:text-red-700 text-xs font-medium px-2">Rejeter</button>
                  <button (click)="edit(c)"
                    class="text-primary-600 hover:text-primary-800 text-xs font-medium px-2">Modifier</button>
                </div>
              </td>
            </tr>
            <tr *ngIf="items.length === 0">
              <td colspan="9" class="px-4 py-12 text-center text-secondary-400">Aucun chèque/effet trouvé.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class ChequesEffetsComponent implements OnInit {
  items: ChequeEffet[] = [];
  loading = true;
  showForm = false;
  editId: number | null = null;
  saving = false;
  error = '';
  form!: FormGroup;

  constructor(
    private service: ChequeEffetService,
    private fb: FormBuilder,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.load();
  }

  initForm(): void {
    const today = new Date().toISOString().split('T')[0];
    this.form = this.fb.group({
      type: ['CHEQUE', Validators.required],
      numero: ['', Validators.required],
      banque: ['', Validators.required],
      montant: [0, [Validators.required, Validators.min(0.01)]],
      dateEmission: [today, Validators.required],
      dateEcheance: [''],
      beneficiaire: ['', Validators.required],
      tireur: [''],
      statut: ['EN_ATTENTE'],
      notes: ['']
    });
  }

  resetForm(): void { this.editId = null; this.error = ''; this.initForm(); }

  load(): void {
    this.loading = true;
    this.service.getAll(0, 50).subscribe({
      next: p => { this.items = p.content; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  edit(c: ChequeEffet): void {
    this.editId = c.id!;
    this.showForm = true;
    this.error = '';
    this.form.patchValue(c);
  }

  changeStatut(id: number, statut: string): void {
    this.service.updateStatut(id, statut).subscribe(() => this.load());
  }

  submit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true; this.error = '';
    const req$ = this.editId
      ? this.service.update(this.editId, this.form.value)
      : this.service.create(this.form.value);
    req$.subscribe({
      next: () => { this.showForm = false; this.saving = false; this.load(); },
      error: err => { this.error = err?.error?.message ?? 'Erreur.'; this.saving = false; }
    });
  }

  statutCss(s?: string): string {
    const m: Record<string, string> = {
      EN_ATTENTE: 'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-700',
      ENCAISSE:   'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-700',
      REJETE:     'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-700',
      RETOURNE:   'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-orange-100 text-orange-700',
      ANNULE:     'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-gray-200 text-gray-500'
    };
    return m[s ?? ''] ?? m['EN_ATTENTE'];
  }
}
