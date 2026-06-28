import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AttachementService, Attachement } from '../../../core/services/attachement.service';
import { BpdeLigneService, BpdeLigne } from '../../../core/services/bpde-ligne.service';
import { ProjetService } from '../../../core/services/projet.service';

@Component({
  selector: 'app-attachement-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="p-6 max-w-4xl mx-auto">
      <div class="flex items-center gap-3 mb-6">
        <button (click)="router.navigate(['/attachements'])"
          class="text-secondary-500 hover:text-secondary-700">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>
        <h1 class="text-xl font-bold text-secondary-900">
          {{ isEdit ? 'Modifier l\'attachement' : 'Nouvel attachement' }}
        </h1>
      </div>

      <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-6">
        <div class="bg-white rounded-xl border border-gray-200 p-6 space-y-4">
          <h2 class="font-semibold text-secondary-800">Informations générales</h2>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Numéro *</label>
              <input formControlName="numero" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary-500 focus:border-primary-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Période *</label>
              <input formControlName="periode" type="month" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary-500 focus:border-primary-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Projet *</label>
              <select formControlName="projetId" (change)="onProjetChange()" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary-500 focus:border-primary-500">
                <option value="">Sélectionner un projet</option>
                <option *ngFor="let p of projets" [value]="p.id">{{ p.nom }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Date *</label>
              <input formControlName="dateAttachement" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary-500 focus:border-primary-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-secondary-700 mb-1">Statut</label>
              <select formControlName="statut" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm">
                <option value="BROUILLON">Brouillon</option>
                <option value="SOUMIS">Soumis</option>
                <option value="VALIDE">Validé</option>
              </select>
            </div>
            <div class="col-span-2">
              <label class="block text-sm font-medium text-secondary-700 mb-1">Description</label>
              <textarea formControlName="description" rows="2" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary-500 focus:border-primary-500"></textarea>
            </div>
          </div>
        </div>

        <!-- Lignes table -->
        <div class="bg-white rounded-xl border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-4">
            <h2 class="font-semibold text-secondary-800">Lignes (quantités sans prix)</h2>
            <button type="button" (click)="addLigne()"
              class="text-sm text-primary-600 hover:text-primary-800 font-medium flex items-center gap-1">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
              </svg>
              Ajouter une ligne
            </button>
          </div>

          <div *ngIf="bpdeLignes.length > 0" class="mb-3 text-xs text-secondary-500">
            Conseil : Liez chaque ligne à une ligne BPDE pour pouvoir générer une facture ultérieurement.
          </div>

          <table class="w-full text-sm">
            <thead class="bg-gray-50">
              <tr>
                <th class="text-left px-3 py-2 font-medium text-secondary-600">Désignation</th>
                <th class="text-left px-3 py-2 font-medium text-secondary-600">Unité</th>
                <th class="text-left px-3 py-2 font-medium text-secondary-600">Qté prévue</th>
                <th class="text-left px-3 py-2 font-medium text-secondary-600">Qté réalisée</th>
                <th class="text-left px-3 py-2 font-medium text-secondary-600">BPDE</th>
                <th class="text-left px-3 py-2 font-medium text-secondary-600">Alerte</th>
                <th></th>
              </tr>
            </thead>
            <tbody formArrayName="lignes">
              <tr *ngFor="let ligne of lignesArray.controls; let i = index"
                [formGroupName]="i"
                [class.bg-orange-50]="isLigneEnAlerte(i)">
                <td class="px-2 py-1">
                  <input formControlName="designation" class="w-full border border-gray-300 rounded px-2 py-1 text-sm" placeholder="Désignation">
                </td>
                <td class="px-2 py-1 w-20">
                  <input formControlName="unite" class="w-full border border-gray-300 rounded px-2 py-1 text-sm" placeholder="m²">
                </td>
                <td class="px-2 py-1 w-28">
                  <input formControlName="quantitePrevue" type="number" min="0" step="0.01"
                    (input)="checkAlerte(i)"
                    class="w-full border border-gray-300 rounded px-2 py-1 text-sm">
                </td>
                <td class="px-2 py-1 w-28">
                  <input formControlName="quantiteRealisee" type="number" min="0" step="0.01"
                    (input)="checkAlerte(i)"
                    class="w-full border border-gray-300 rounded px-2 py-1 text-sm">
                </td>
                <td class="px-2 py-1 w-40">
                  <select formControlName="bpdeLigneId" class="w-full border border-gray-300 rounded px-2 py-1 text-sm">
                    <option value="">—</option>
                    <option *ngFor="let b of bpdeLignes" [value]="b.id">{{ b.designation }}</option>
                  </select>
                </td>
                <td class="px-2 py-1 w-16 text-center">
                  <span *ngIf="isLigneEnAlerte(i)" class="text-orange-500" title="Quantité réalisée > prévue">
                    <svg class="w-4 h-4 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
                    </svg>
                  </span>
                </td>
                <td class="px-2 py-1 w-8">
                  <button type="button" (click)="removeLigne(i)"
                    class="text-red-400 hover:text-red-600">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>

          <div *ngIf="lignesArray.length === 0" class="text-center text-secondary-400 text-sm py-6">
            Aucune ligne. Cliquez sur "Ajouter une ligne" pour commencer.
          </div>
        </div>

        <div *ngIf="error" class="bg-red-50 border border-red-200 rounded-lg p-3 text-red-700 text-sm">{{ error }}</div>

        <div class="flex justify-end gap-3">
          <button type="button" (click)="router.navigate(['/attachements'])"
            class="px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-secondary-700 hover:bg-gray-50">
            Annuler
          </button>
          <button type="submit" [disabled]="saving"
            class="px-6 py-2 bg-primary-600 text-white rounded-lg text-sm font-medium hover:bg-primary-700 disabled:opacity-50">
            {{ saving ? 'Enregistrement...' : (isEdit ? 'Mettre à jour' : 'Créer') }}
          </button>
        </div>
      </form>
    </div>
  `
})
export class AttachementFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  saving = false;
  error = '';
  projets: any[] = [];
  bpdeLignes: BpdeLigne[] = [];

  constructor(
    private fb: FormBuilder,
    private attachementService: AttachementService,
    private bpdeLigneService: BpdeLigneService,
    private projetService: ProjetService,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      numero: ['', Validators.required],
      periode: ['', Validators.required],
      projetId: ['', Validators.required],
      dateAttachement: ['', Validators.required],
      statut: ['BROUILLON'],
      description: [''],
      lignes: this.fb.array([])
    });

    this.projetService.getAllProjets(0, 200, 'nom').subscribe(p => this.projets = p.content);

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.attachementService.getById(+id).subscribe(att => {
        this.form.patchValue({ ...att, projetId: att.projetId?.toString() });
        att.lignes?.forEach(l => this.addLigne(l));
        if (att.projetId) this.loadBpde(att.projetId);
      });
    }
  }

  get lignesArray(): FormArray { return this.form.get('lignes') as FormArray; }

  addLigne(data?: any): void {
    this.lignesArray.push(this.fb.group({
      designation: [data?.designation ?? '', Validators.required],
      unite: [data?.unite ?? ''],
      quantitePrevue: [data?.quantitePrevue ?? 0, [Validators.required, Validators.min(0)]],
      quantiteRealisee: [data?.quantiteRealisee ?? 0, [Validators.required, Validators.min(0)]],
      bpdeLigneId: [data?.bpdeLigneId ?? '']
    }));
  }

  removeLigne(i: number): void { this.lignesArray.removeAt(i); }

  isLigneEnAlerte(i: number): boolean {
    const g = this.lignesArray.at(i);
    const prevue = +g.get('quantitePrevue')?.value;
    const realisee = +g.get('quantiteRealisee')?.value;
    return realisee > prevue && prevue > 0;
  }

  checkAlerte(_i: number): void { /* triggers re-render via change detection */ }

  onProjetChange(): void {
    const pid = +this.form.get('projetId')?.value;
    if (pid) this.loadBpde(pid);
  }

  loadBpde(projetId: number): void {
    this.bpdeLigneService.getByProjet(projetId).subscribe(l => this.bpdeLignes = l);
  }

  submit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    this.error = '';

    const payload: Partial<Attachement> = {
      ...this.form.value,
      projetId: +this.form.value.projetId,
      lignes: this.lignesArray.value.map((l: any) => ({
        ...l,
        bpdeLigneId: l.bpdeLigneId ? +l.bpdeLigneId : null
      }))
    };

    const id = this.route.snapshot.paramMap.get('id');
    const req$ = id
      ? this.attachementService.update(+id, payload)
      : this.attachementService.create(payload);

    req$.subscribe({
      next: () => this.router.navigate(['/attachements']),
      error: err => {
        this.error = err?.error?.message ?? 'Une erreur est survenue.';
        this.saving = false;
      }
    });
  }
}
