import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/page.model';
import { AffectationEmploye } from '../models/affectation-employe.model';

@Injectable({
  providedIn: 'root'
})
export class AffectationEmployeService {
  private apiUrl = `${environment.apiUrl}/affectations`;

  constructor(private http: HttpClient) { }

  getAll(page: number, size: number, sort: string): Observable<Page<AffectationEmploye>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<AffectationEmploye>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<AffectationEmploye> {
    return this.http.get<AffectationEmploye>(`${this.apiUrl}/${id}`);
  }

  create(affectation: Partial<AffectationEmploye>): Observable<AffectationEmploye> {
    return this.http.post<AffectationEmploye>(this.apiUrl, affectation);
  }

  update(id: number, affectation: Partial<AffectationEmploye>): Observable<AffectationEmploye> {
    return this.http.put<AffectationEmploye>(`${this.apiUrl}/${id}`, affectation);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
