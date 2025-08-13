import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { AppelOffre } from '../models/appel-offre.model';

@Injectable({
  providedIn: 'root'
})
export class AppelOffreService {
  private apiUrl = '/api/appel-offres';

  constructor(private http: HttpClient) { }

  getAllAppelOffres(page: number, size: number, sort: string): Observable<Page<AppelOffre>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<AppelOffre>>(this.apiUrl, { params });
  }

  getAppelOffreById(id: number): Observable<AppelOffre> {
    return this.http.get<AppelOffre>(`${this.apiUrl}/${id}`);
  }

  createAppelOffre(appelOffre: Partial<AppelOffre>): Observable<AppelOffre> {
    return this.http.post<AppelOffre>(this.apiUrl, appelOffre);
  }

  updateAppelOffre(id: number, appelOffre: Partial<AppelOffre>): Observable<AppelOffre> {
    return this.http.put<AppelOffre>(`${this.apiUrl}/${id}`, appelOffre);
  }

  deleteAppelOffre(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  findByStatut(statut: string, pageable: HttpParams): Observable<Page<AppelOffre>> {
    return this.http.get<Page<AppelOffre>>(`${this.apiUrl}/statut/${statut}`, { params: pageable });
  }

  findActiveAppelOffres(pageable: HttpParams): Observable<Page<AppelOffre>> {
    return this.http.get<Page<AppelOffre>>(`${this.apiUrl}/search/active`, { params: pageable });
  }

  findByCreator(userId: number, pageable: HttpParams): Observable<Page<AppelOffre>> {
    return this.http.get<Page<AppelOffre>>(`${this.apiUrl}/search/by-creator/${userId}`, { params: pageable });
  }
}
