import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Caution } from '../models/caution.model';
import { environment } from '../../../environments/environment'; // IDE might flag this

@Injectable({
  providedIn: 'root'
})
export class CautionService {
  private apiUrl = '${environment.apiUrl}/cautions';

  constructor(private http: HttpClient) { }

  getAllCautions(page: number, size: number, sort: string): Observable<Page<Caution>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Caution>>(this.apiUrl, { params });
  }

  getCautionById(id: number): Observable<Caution> {
    return this.http.get<Caution>(`${this.apiUrl}/${id}`);
  }

  createCaution(caution: Partial<Caution>): Observable<Caution> {
    return this.http.post<Caution>(this.apiUrl, caution);
  }

  updateCaution(id: number, caution: Partial<Caution>): Observable<Caution> {
    return this.http.put<Caution>(`${this.apiUrl}/${id}`, caution);
  }

  deleteCaution(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  findByType(type: string, pageable: HttpParams): Observable<Page<Caution>> {
    return this.http.get<Page<Caution>>(`${this.apiUrl}/type/${type}`, { params: pageable });
  }

  findByStatut(statut: string, pageable: HttpParams): Observable<Page<Caution>> {
    return this.http.get<Page<Caution>>(`${this.apiUrl}/statut/${statut}`, { params: pageable });
  }

  findByProjet(projetId: number, pageable: HttpParams): Observable<Page<Caution>> {
    return this.http.get<Page<Caution>>(`${this.apiUrl}/projet/${projetId}`, { params: pageable });
  }

  findExpiredCautions(pageable: HttpParams): Observable<Page<Caution>> {
    return this.http.get<Page<Caution>>(`${this.apiUrl}/search/expired`, { params: pageable });
  }

  findExpiringSoon(date: string, pageable: HttpParams): Observable<Page<Caution>> {
    const params = pageable.set('date', date);
    return this.http.get<Page<Caution>>(`${this.apiUrl}/search/expiring-soon`, { params });
  }

  findByBanque(banque: string, pageable: HttpParams): Observable<Page<Caution>> {
    const params = pageable.set('banque', banque);
    return this.http.get<Page<Caution>>(`${this.apiUrl}/search/by-banque`, { params });
  }
}
