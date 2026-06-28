import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { environment } from '../../../environments/environment';

export interface Paiement {
  id?: number;
  factureId: number;
  factureNumero?: string;
  montant: number;
  datePaiement: string;
  modePaiement: 'VIREMENT' | 'CHEQUE' | 'ESPECES' | 'EFFET';
  reference?: string;
  notes?: string;
}

@Injectable({ providedIn: 'root' })
export class PaiementService {
  private apiUrl = `${environment.apiUrl}/paiements`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 20): Observable<Page<Paiement>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Paiement>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<Paiement> {
    return this.http.get<Paiement>(`${this.apiUrl}/${id}`);
  }

  getByFacture(factureId: number, page = 0, size = 50): Observable<Page<Paiement>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Paiement>>(`${this.apiUrl}/facture/${factureId}`, { params });
  }

  create(dto: Partial<Paiement>): Observable<Paiement> {
    return this.http.post<Paiement>(this.apiUrl, dto);
  }

  update(id: number, dto: Partial<Paiement>): Observable<Paiement> {
    return this.http.put<Paiement>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
