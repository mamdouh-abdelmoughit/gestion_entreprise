import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Decompte } from '../models/decompte.model';
import { environment } from '../../../environments/environment'; // IDE might flag this

@Injectable({
  providedIn: 'root'
})
export class DecompteService {
  private apiUrl = '${environment.apiUrl}/decomptes';

  constructor(private http: HttpClient) { }

  getAllDecomptes(page: number, size: number, sort: string): Observable<Page<Decompte>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Decompte>>(this.apiUrl, { params });
  }

  getDecompteById(id: number): Observable<Decompte> {
    return this.http.get<Decompte>(`${this.apiUrl}/${id}`);
  }

  createDecompte(decompte: Partial<Decompte>): Observable<Decompte> {
    return this.http.post<Decompte>(this.apiUrl, decompte);
  }

  updateDecompte(id: number, decompte: Partial<Decompte>): Observable<Decompte> {
    return this.http.put<Decompte>(`${this.apiUrl}/${id}`, decompte);
  }

  deleteDecompte(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  findByProjet(projetId: number, pageable: HttpParams): Observable<Page<Decompte>> {
    return this.http.get<Page<Decompte>>(`${this.apiUrl}/projet/${projetId}`, { params: pageable });
  }

  findByStatut(statut: string, pageable: HttpParams): Observable<Page<Decompte>> {
    return this.http.get<Page<Decompte>>(`${this.apiUrl}/statut/${statut}`, { params: pageable });
  }

  findByDateRange(startDate: string, endDate: string, pageable: HttpParams): Observable<Page<Decompte>> {
    const params = pageable.set('startDate', startDate).set('endDate', endDate);
    return this.http.get<Page<Decompte>>(`${this.apiUrl}/search/by-date-range`, { params });
  }
}
