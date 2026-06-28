import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PaiementService, Paiement } from '../../core/services/paiement.service';
import { FactureService, Facture } from '../../core/services/facture.service';

@Component({
  selector: 'app-paiements',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="p-6">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-bold text-secondary-900">Paiements</h1>
          <p *ngIf="factureFiltered" class="text-sm text-secondary-500 mt-1">
            Facture : <span class="font-medium text-secondary-700">{{ factureFiltered.numero }}</span>
            — Reste à payer : <span class="font-semibold text-red-600">{{ factureFiltered.resteAPayer | number:'1.2-2' }} MAD</span>
          </p>
        </div>
        <div class="flex gap-3">
          <button *ngIf="factureId" (click)="router.navigate(['/factures'])"
            class="px-3 py-2 border border-gray-300 rounded-lg text-sm text-secondary-700 hover:bg-gray-50">
            Retour factures
          </button>
          <button (click)="showForm = true; resetForm()"
            class="bg-primary-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-primary-700 flex items-center gap-2">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
            </svg>
            Nouveau paiement
          </button>
        </div>
      </div>

      <!-- Inline form -->
      <div *ngIf="showForm" class="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h2 class="font-semibold text-secondary-800 mb-4">{{ editId ? 'Modifier le paiement' : 'Enregistrer un paiement' }}</h2>
        <form [formGroup]="form" (ngSubmit)="submit()" class="grid grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Facture *</label>
            <select formControlName="factureId" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="">Sélectionner</option>
              <option *ngFor="let f of factures" [value]="f.id">{{ f.numero }} ({{ f.resteAPayer | number:'1.0-0' }} MAD restant)</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Montant (MAD) *</label>
            <input formControlName="montant" type="number" min="0.01" step="0.01"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Date *</label>
            <input formControlName="datePaiement" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Mode de paiement *</label>
            <select formControlName="modePaiement" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="VIREMENT">Virement</option>
              <option value="CHEQUE">Chèque</option>
              <option value="ESPECES">Espèces</option>
              <option value="EFFET">Effet</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Référence</label>
            <input formControlName="reference" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm" placeholder="N° virement, chèque...">
          </div>
          <div>
            <label class="block text-sm font-medium text-secondary-700 mb-1">Notes</label>
            <input formControlName="notes" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
          </div>
          <div *ngIf="error" class="col-span-3 bg-red-50 border border-red-200 rounded-lg p-3 text-red-700 text-sm">{{ error }}</div>
          <div class="col-span-3 flex justify-end gap-3">
            <button type="button" (click)="showForm = false"
              class="px-4 py-2 border border-gray-300 rounded-lg text-sm text-secondary-700">Annuler</button>
            <button type="submit" [disabled]="saving"
              class="px-5 py-2 bg-primary-600 text-white rounded-lg text-sm font-medium disabled:opacity-50">
              {{ saving ? '...' : (editId ? 'Mettre à jour' : 'Enregistrer') }}
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
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Facture</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Date</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Mode</th>
              <th class="text-left px-4 py-3 font-medium text-secondary-600">Référence</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Montant</th>
              <th class="text-right px-4 py-3 font-medium text-secondary-600">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr *ngFor="let p of paiements" class="hover:bg-gray-50">
              <td class="px-4 py-3 text-secondary-600">{{ p.factureNumero }}</td>
              <td class="px-4 py-3 text-secondary-600">{{ p.datePaiement | date:'dd/MM/yyyy' }}</td>
              <td class="px-4 py-3">
                <span [class]="modeCss(p.modePaiement)">{{ p.modePaiement }}</span>
              </td>
              <td class="px-4 py-3 text-secondary-500 text-xs">{{ p.reference || '—' }}</td>
              <td class="px-4 py-3 text-right font-semibold text-green-700">{{ p.montant | number:'1.2-2' }} MAD</td>
              <td class="px-4 py-3 text-right">
                <div class="flex justify-end gap-2">
                  <button (click)="edit(p)" class="text-primary-600 hover:text-primary-800 text-xs font-medium">Modifier</button>
                  <button (click)="delete(p.id!)" class="text-red-500 hover:text-red-700 text-xs font-medium">Supprimer</button>
                </div>
              </td>
            </tr>
            <tr *ngIf="paiements.length === 0">
              <td colspan="6" class="px-4 py-12 text-center text-secondary-400">Aucun paiement enregistré.</td>
            </tr>
          </tbody>
          <tfoot *ngIf="paiements.length > 0" class="bg-gray-50 border-t border-gray-200">
            <tr>
              <td colspan="4" class="px-4 py-2 text-sm font-medium text-secondary-700">Total versé</td>
              <td class="px-4 py-2 text-right font-bold text-green-700">{{ totalVerse | number:'1.2-2' }} MAD</td>
              <td></td>
            </tr>
          </tfoot>
        </table>
      </div>
    </div>
  `
})
export class PaiementsComponent implements OnInit {
  paiements: Paiement[] = [];
  factures: Facture[] = [];
  factureFiltered?: Facture;
  factureId?: number;
  loading = true;
  showForm = false;
  editId: number | null = null;
  saving = false;
  error = '';
  form!: FormGroup;

  get totalVerse(): number {
    return this.paiements.reduce((s, p) => s + (p.montant ?? 0), 0);
  }

  constructor(
    private paiementService: PaiementService,
    private factureService: FactureService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    const fid = this.route.snapshot.queryParamMap.get('factureId');
    if (fid) {
      this.factureId = +fid;
      this.factureService.getById(this.factureId).subscribe(f => { this.factureFiltered = f; });
    }
    this.factureService.getAll(0, 200).subscribe(p => this.factures = p.content);
    this.load();
  }

  initForm(): void {
    const today = new Date().toISOString().split('T')[0];
    this.form = this.fb.group({
      factureId: [this.factureId?.toString() ?? '', Validators.required],
      montant: [0, [Validators.required, Validators.min(0.01)]],
      datePaiement: [today, Validators.required],
      modePaiement: ['VIREMENT', Validators.required],
      reference: [''],
      notes: ['']
    });
  }

  resetForm(): void {
    this.editId = null; this.error = '';
    this.initForm();
    if (this.factureId) this.form.patchValue({ factureId: this.factureId.toString() });
  }

  load(): void {
    this.loading = true;
    const obs$ = this.factureId
      ? this.paiementService.getByFacture(this.factureId)
      : this.paiementService.getAll(0, 50);
    obs$.subscribe({
      next: page => { this.paiements = page.content; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  edit(p: Paiement): void {
    this.editId = p.id!; this.showForm = true; this.error = '';
    this.form.patchValue({ ...p, factureId: p.factureId?.toString() });
  }

  delete(id: number): void {
    if (!confirm('Supprimer ce paiement ?')) return;
    this.paiementService.delete(id).subscribe(() => this.load());
  }

  submit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true; this.error = '';
    const val = { ...this.form.value, factureId: +this.form.value.factureId };
    const req$ = this.editId ? this.paiementService.update(this.editId, val) : this.paiementService.create(val);
    req$.subscribe({
      next: () => { this.showForm = false; this.saving = false; this.load(); },
      error: err => { this.error = err?.error?.message ?? 'Erreur.'; this.saving = false; }
    });
  }

  modeCss(mode?: string): string {
    const m: Record<string, string> = {
      VIREMENT: 'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-700',
      CHEQUE:   'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-700',
      ESPECES:  'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-700',
      EFFET:    'inline-block px-2 py-0.5 rounded-full text-xs font-medium bg-teal-100 text-teal-700'
    };
    return m[mode ?? ''] ?? m['VIREMENT'];
  }
}
