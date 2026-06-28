import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { environment } from '../../../environments/environment';

export interface Facture {
  id?: number;
  numero: string;
  projetId: number;
  projetNom?: string;
  attachementId?: number;
  attachementNumero?: string;
  type: 'CLIENT' | 'FOURNISSEUR';
  dateEmission: string;
  dateEcheance: string;
  montantHT: number;
  tva: number;
  montantTTC: number;
  retenuGarantie: number;
  avance: number;
  montantNet: number;
  statut?: string;
  observations?: string;
  totalPaye?: number;
  resteAPayer?: number;
}

@Injectable({ providedIn: 'root' })
export class FactureService {
  private apiUrl = `${environment.apiUrl}/factures`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 20): Observable<Page<Facture>> {
    const params = new HttpParams().set('page', page).set('size', size).set('sort', 'dateEmission,desc');
    return this.http.get<Page<Facture>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<Facture> {
    return this.http.get<Facture>(`${this.apiUrl}/${id}`);
  }

  getByProjet(projetId: number, page = 0, size = 50): Observable<Page<Facture>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Facture>>(`${this.apiUrl}/projet/${projetId}`, { params });
  }

  create(dto: Partial<Facture>): Observable<Facture> {
    return this.http.post<Facture>(this.apiUrl, dto);
  }

  generateFromAttachement(attachementId: number, params: {
    numero: string; tva?: number; retenuGarantie?: number; avance?: number;
  }): Observable<Facture> {
    return this.http.post<Facture>(`${this.apiUrl}/generate-from-attachement/${attachementId}`, params);
  }

  update(id: number, dto: Partial<Facture>): Observable<Facture> {
    return this.http.put<Facture>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
