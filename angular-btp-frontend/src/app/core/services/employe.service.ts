import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Employe } from '../models/employe.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeService {
  private apiUrl = '/api/employes';

  constructor(private http: HttpClient) { }

  getAllEmployes(page: number, size: number, sort: string): Observable<Page<Employe>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Employe>>(this.apiUrl, { params });
  }

  getEmployeById(id: number): Observable<Employe> {
    return this.http.get<Employe>(`${this.apiUrl}/${id}`);
  }

  createEmploye(employe: Partial<Employe>): Observable<Employe> {
    return this.http.post<Employe>(this.apiUrl, employe);
  }

  updateEmploye(id: number, employe: Partial<Employe>): Observable<Employe> {
    return this.http.put<Employe>(`${this.apiUrl}/${id}`, employe);
  }

  deleteEmploye(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  findByStatut(statut: string, page: number, size: number, sort: string): Observable<Page<Employe>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Employe>>(`${this.apiUrl}/statut/${statut}`, { params });
  }

  findByPoste(poste: string, page: number, size: number, sort: string): Observable<Page<Employe>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Employe>>(`${this.apiUrl}/poste/${poste}`, { params });
  }

  searchByName(keyword: string, page: number, size: number, sort: string): Observable<Page<Employe>> {
    const params = new HttpParams().set('keyword', keyword).set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Employe>>(`${this.apiUrl}/search/by-name`, { params });
  }

  findByCreator(userId: number, page: number, size: number, sort: string): Observable<Page<Employe>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString()).set('sort', sort);
    return this.http.get<Page<Employe>>(`${this.apiUrl}/search/by-creator/${userId}`, { params });
  }

  findByStatutAndCreator(statut: string, userId: number, pageable: HttpParams): Observable<Page<Employe>> {
    return this.http.get<Page<Employe>>(`${this.apiUrl}/search/by-statut/${statut}/by-creator/${userId}`, { params: pageable });
  }
}
