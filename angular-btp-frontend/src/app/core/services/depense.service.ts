import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Depense } from '../models/depense.model';

@Injectable({
  providedIn: 'root'
})
export class DepenseService {
  private apiUrl = '/api/depenses';

  constructor(private http: HttpClient) { }

  getAllDepenses(page: number, size: number, sort: string): Observable<Page<Depense>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Depense>>(this.apiUrl, { params });
  }

  getDepenseById(id: number): Observable<Depense> {
    return this.http.get<Depense>(`${this.apiUrl}/${id}`);
  }

  createDepense(depense: Partial<Depense>): Observable<Depense> {
    return this.http.post<Depense>(this.apiUrl, depense);
  }

  updateDepense(id: number, depense: Partial<Depense>): Observable<Depense> {
    return this.http.put<Depense>(`${this.apiUrl}/${id}`, depense);
  }

  deleteDepense(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
