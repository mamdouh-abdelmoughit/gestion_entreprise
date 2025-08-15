import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Fournisseur } from '../models/fournisseur.model';
import { environment } from '../../../environments/environment'; // IDE might flag this


@Injectable({
  providedIn: 'root'
})
export class FournisseurService {
  private apiUrl = '${environment.apiUrl}/fournisseurs';

  constructor(private http: HttpClient) { }

  getAllFournisseurs(page: number, size: number, sort: string): Observable<Page<Fournisseur>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Fournisseur>>(this.apiUrl, { params });
  }
  getFournisseurById(id: number): Observable<Fournisseur> {
    return this.http.get<Fournisseur>(`${this.apiUrl}/${id}`);
  }

  createFournisseur(fournisseur: Partial<Fournisseur>): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(this.apiUrl, fournisseur);
  }

  updateFournisseur(id: number, fournisseur: Partial<Fournisseur>): Observable<Fournisseur> {
    return this.http.put<Fournisseur>(`${this.apiUrl}/${id}`, fournisseur);
  }

  deleteFournisseur(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  findByType(type: string, pageable: HttpParams): Observable<Page<Fournisseur>> {
    return this.http.get<Page<Fournisseur>>(`${this.apiUrl}/type/${type}`, { params: pageable });
  }

  findByStatut(statut: string, pageable: HttpParams): Observable<Page<Fournisseur>> {
    return this.http.get<Page<Fournisseur>>(`${this.apiUrl}/statut/${statut}`, { params: pageable });
  }

  searchByName(keyword: string, pageable: HttpParams): Observable<Page<Fournisseur>> {
    const params = pageable.set('keyword', keyword);
    return this.http.get<Page<Fournisseur>>(`${this.apiUrl}/search/by-name`, { params });
  }

  findBySpecialite(specialite: string, pageable: HttpParams): Observable<Page<Fournisseur>> {
    return this.http.get<Page<Fournisseur>>(`${this.apiUrl}/search/by-specialite/${specialite}`, { params: pageable });
  }

  findByCreator(userId: number, pageable: HttpParams): Observable<Page<Fournisseur>> {
    return this.http.get<Page<Fournisseur>>(`${this.apiUrl}/search/by-creator/${userId}`, { params: pageable });
  }

  findByIce(ice: string, pageable: HttpParams): Observable<Page<Fournisseur>> {
    return this.http.get<Page<Fournisseur>>(`${this.apiUrl}/search/by-ice/${ice}`, { params: pageable });
  }
}


