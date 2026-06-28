import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FactureService, Facture } from '../../../core/services/facture.service';
import { ProjetService } from '../../../core/services/projet.service';
import { AttachementService, Attachement } from '../../../core/services/attachement.service';

@Component({
  selector: 'app-facture-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule],
  template: `
    <div class="p-6 max-w-3xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button (click)="router.navigate(['/factures'])" class="text-secondary-500 hover:text-secondary-700">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>
        <h1 class="text-xl font-bold text-secondary-900">
          {{ isEdit ? 'Modifier la facture' : 'Nouvelle facture' }}
        </h1>
      </div>

      <!-- Generate from attachement -->
      <div *ngIf="!isEdit" class="bg-blue-50 border border-blue-200 rounded-xl p-4 mb-6">
        <p class="text-sm text-blue-700 font-medium mb-3">Générer depuis un attachement validé</p>
        <div class="flex gap-3">
          <select [(ngModel)]="selectedAttachementId" [ngModelOptions]="{standalone: true}"
            class="flex-1 border border-blue-300 rounded-lg px-3 py-2 text-sm">
            <option value="">Sélectionner un attachement...</option>
            <option *ngFor="let a of attachements" [value]="a.id">{{ a.numero }} — {{ a.projetNom }} ({{ a.periode }})</option>
          </select>
          <button type="button" (click)="generateFromAttachement()"
            [disabled]="!selectedAttachementId"
            class="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-blue-700 disabled:opacity-40">
            Générer
          </button>
        </div>
      </div>

      <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-5">
        <div class="bg-white rounded-xl border border-gray-200 p-6">
          <h2 class="font-semibold text-secondary-800 mb-4">Informations facture</h2>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Numéro *</label>
              <input formControlName="numero" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Type *</label>
              <select formControlName="type" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
                <option value="CLIENT">Client</option>
                <option value="FOURNISSEUR">Fournisseur</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Projet *</label>
              <select formControlName="projetId" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
                <option value="">Sélectionner un projet</option>
                <option *ngFor="let p of projets" [value]="p.id">{{ p.nom }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Statut</label>
              <select formControlName="statut" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
                <option value="BROUILLON">Brouillon</option>
                <option value="EMISE">Émise</option>
                <option value="PARTIELLEMENT_PAYEE">Partiellement payée</option>
                <option value="PAYEE">Payée</option>
                <option value="EN_RETARD">En retard</option>
                <option value="ANNULEE">Annulée</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Date d'émission *</label>
              <input formControlName="dateEmission" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Date d'échéance *</label>
              <input formControlName="dateEcheance" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl border border-gray-200 p-6">
          <h2 class="font-semibold text-secondary-800 mb-4">Montants</h2>
          <div class="grid grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Montant HT (MAD)</label>
              <input formControlName="montantHT" type="number" min="0" step="0.01" (input)="recalculate()" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">TVA (%)</label>
              <input formControlName="tva" type="number" min="0" step="0.1" (input)="recalculate()" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Montant TTC (MAD)</label>
              <input formControlName="montantTTC" type="number" readonly class="w-full border border-gray-200 bg-gray-50 rounded-lg px-3 py-2 text-sm text-secondary-600">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Retenue garantie (MAD)</label>
              <input formControlName="retenuGarantie" type="number" min="0" step="0.01" (input)="recalculate()" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Avance (MAD)</label>
              <input formControlName="avance" type="number" min="0" step="0.01" (input)="recalculate()" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Montant net (MAD)</label>
              <input formControlName="montantNet" type="number" readonly class="w-full border border-gray-200 bg-green-50 rounded-lg px-3 py-2 text-sm font-semibold text-green-700">
            </div>
          </div>
          <div class="mt-4">
            <label class="block text-sm font-medium text-secondary-700 mb-1">Observations</label>
            <textarea formControlName="observations" rows="2" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"></textarea>
          </div>
        </div>

        <div *ngIf="error" class="bg-red-50 border border-red-200 rounded-lg p-3 text-red-700 text-sm">{{ error }}</div>

        <div class="flex justify-end gap-3">
          <button type="button" (click)="router.navigate(['/factures'])"
            class="px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-secondary-700 hover:bg-gray-50">Annuler</button>
          <button type="submit" [disabled]="saving"
            class="px-6 py-2 bg-primary-600 text-white rounded-lg text-sm font-medium hover:bg-primary-700 disabled:opacity-50">
            {{ saving ? 'Enregistrement...' : (isEdit ? 'Mettre à jour' : 'Créer') }}
          </button>
        </div>
      </form>
    </div>
  `
})
export class FactureFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  saving = false;
  error = '';
  projets: any[] = [];
  attachements: Attachement[] = [];
  selectedAttachementId: number | '' = '';

  constructor(
    private fb: FormBuilder,
    private factureService: FactureService,
    private projetService: ProjetService,
    private attachementService: AttachementService,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    const in30 = new Date(Date.now() + 30 * 86400000).toISOString().split('T')[0];

    this.form = this.fb.group({
      numero: ['', Validators.required],
      type: ['CLIENT', Validators.required],
      projetId: ['', Validators.required],
      statut: ['BROUILLON'],
      dateEmission: [today, Validators.required],
      dateEcheance: [in30, Validators.required],
      montantHT: [0],
      tva: [20],
      montantTTC: [{ value: 0, disabled: true }],
      retenuGarantie: [0],
      avance: [0],
      montantNet: [{ value: 0, disabled: true }],
      observations: ['']
    });

    this.projetService.getAllProjets(0, 200, 'nom').subscribe(p => this.projets = p.content);
    this.attachementService.getAll(0, 200).subscribe(p => this.attachements = p.content.filter(a => a.statut === 'VALIDE'));

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.factureService.getById(+id).subscribe(f => {
        this.form.patchValue({ ...f, projetId: f.projetId?.toString() });
      });
    }
  }

  recalculate(): void {
    const ht = +this.form.get('montantHT')?.value ?? 0;
    const tva = +this.form.get('tva')?.value ?? 0;
    const ttc = ht * (1 + tva / 100);
    const rg = +this.form.get('retenuGarantie')?.value ?? 0;
    const av = +this.form.get('avance')?.value ?? 0;
    this.form.patchValue({ montantTTC: +ttc.toFixed(2), montantNet: +(ttc - rg - av).toFixed(2) }, { emitEvent: false });
  }

  generateFromAttachement(): void {
    if (!this.selectedAttachementId) return;
    const numero = this.form.get('numero')?.value;
    if (!numero) { this.error = 'Veuillez d\'abord saisir un numéro de facture.'; return; }
    this.saving = true;
    this.factureService.generateFromAttachement(+this.selectedAttachementId, {
      numero,
      tva: +this.form.get('tva')?.value,
      retenuGarantie: +this.form.get('retenuGarantie')?.value,
      avance: +this.form.get('avance')?.value
    }).subscribe({
      next: () => this.router.navigate(['/factures']),
      error: err => { this.error = err?.error?.message ?? 'Erreur génération.'; this.saving = false; }
    });
  }

  submit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    this.error = '';
    const raw = this.form.getRawValue();
    const payload: Partial<Facture> = { ...raw, projetId: +raw.projetId };
    const id = this.route.snapshot.paramMap.get('id');
    const req$ = id ? this.factureService.update(+id, payload) : this.factureService.create(payload);
    req$.subscribe({
      next: () => this.router.navigate(['/factures']),
      error: err => { this.error = err?.error?.message ?? 'Une erreur est survenue.'; this.saving = false; }
    });
  }
}
