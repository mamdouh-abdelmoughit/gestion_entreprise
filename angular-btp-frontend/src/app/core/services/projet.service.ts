import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Projet } from '../models/projet.model';

@Injectable({
  providedIn: 'root'
})
export class ProjetService {
  private apiUrl = '/api/projets';

  constructor(private http: HttpClient) { }

  // Basic CRUD
  getAllProjets(page: number, size: number, sort: string): Observable<Page<Projet>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Projet>>(this.apiUrl, { params });
  }

  getProjetById(id: number): Observable<Projet> {
    return this.http.get<Projet>(`${this.apiUrl}/${id}`);
  }

  createProjet(projet: Partial<Projet>): Observable<Projet> {
    return this.http.post<Projet>(this.apiUrl, projet);
  }

  updateProjet(id: number, projet: Partial<Projet>): Observable<Projet> {
    return this.http.put<Projet>(`${this.apiUrl}/${id}`, projet);
  }

  deleteProjet(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Custom Search Methods
  findActiveProjects(page: number, size: number, sort: string): Observable<Page<Projet>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Projet>>(`${this.apiUrl}/search/active`, { params });
  }

  findByChef(chefId: number, page: number, size: number, sort: string): Observable<Page<Projet>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Projet>>(`${this.apiUrl}/chef/${chefId}`, { params });
  }

  findByStatut(statut: string, page: number, size: number, sort: string): Observable<Page<Projet>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Projet>>(`${this.apiUrl}/statut/${statut}`, { params });
  }

  findByCreator(userId: number, page: number, size: number, sort: string): Observable<Page<Projet>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Projet>>(`${this.apiUrl}/search/by-creator/${userId}`, { params });
  }

  findByStatutAndChef(statut: string, chefId: number, page: number, size: number, sort: string): Observable<Page<Projet>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Projet>>(`${this.apiUrl}/search/by-statut/${statut}/by-chef/${chefId}`, { params });
  }
}

