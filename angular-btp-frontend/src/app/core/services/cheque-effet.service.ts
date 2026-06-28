import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { environment } from '../../../environments/environment';

export interface ChequeEffet {
  id?: number;
  type: 'CHEQUE' | 'EFFET';
  numero: string;
  banque: string;
  montant: number;
  dateEmission: string;
  dateEcheance?: string;
  beneficiaire: string;
  tireur?: string;
  statut?: 'EN_ATTENTE' | 'ENCAISSE' | 'REJETE' | 'RETOURNE' | 'ANNULE';
  projetId?: number;
  projetNom?: string;
  factureId?: number;
  factureNumero?: string;
  notes?: string;
}

@Injectable({ providedIn: 'root' })
export class ChequeEffetService {
  private apiUrl = `${environment.apiUrl}/cheques-effets`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 20): Observable<Page<ChequeEffet>> {
    const params = new HttpParams().set('page', page).set('size', size).set('sort', 'dateEmission,desc');
    return this.http.get<Page<ChequeEffet>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<ChequeEffet> {
    return this.http.get<ChequeEffet>(`${this.apiUrl}/${id}`);
  }

  getByProjet(projetId: number, page = 0, size = 50): Observable<Page<ChequeEffet>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<ChequeEffet>>(`${this.apiUrl}/projet/${projetId}`, { params });
  }

  create(dto: Partial<ChequeEffet>): Observable<ChequeEffet> {
    return this.http.post<ChequeEffet>(this.apiUrl, dto);
  }

  update(id: number, dto: Partial<ChequeEffet>): Observable<ChequeEffet> {
    return this.http.put<ChequeEffet>(`${this.apiUrl}/${id}`, dto);
  }

  updateStatut(id: number, statut: string): Observable<ChequeEffet> {
    return this.http.patch<ChequeEffet>(`${this.apiUrl}/${id}/statut`, null, {
      params: new HttpParams().set('statut', statut)
    });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
