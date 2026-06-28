import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { environment } from '../../../environments/environment';

export interface LigneAttachement {
  id?: number;
  attachementId?: number;
  designation: string;
  unite: string;
  quantitePrevue: number;
  quantiteRealisee: number;
  alerte?: boolean;
  bpdeLigneId?: number;
}

export interface Attachement {
  id?: number;
  numero: string;
  projetId: number;
  projetNom?: string;
  periode: string;
  dateAttachement: string;
  description?: string;
  statut?: string;
  documentPdf?: string;
  lignes?: LigneAttachement[];
  createdById?: number;
  createdByUsername?: string;
}

@Injectable({ providedIn: 'root' })
export class AttachementService {
  private apiUrl = `${environment.apiUrl}/attachements`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 20): Observable<Page<Attachement>> {
    const params = new HttpParams().set('page', page).set('size', size).set('sort', 'dateAttachement,desc');
    return this.http.get<Page<Attachement>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<Attachement> {
    return this.http.get<Attachement>(`${this.apiUrl}/${id}`);
  }

  getByProjet(projetId: number, page = 0, size = 50): Observable<Page<Attachement>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Attachement>>(`${this.apiUrl}/projet/${projetId}`, { params });
  }

  create(dto: Partial<Attachement>): Observable<Attachement> {
    return this.http.post<Attachement>(this.apiUrl, dto);
  }

  update(id: number, dto: Partial<Attachement>): Observable<Attachement> {
    return this.http.put<Attachement>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
