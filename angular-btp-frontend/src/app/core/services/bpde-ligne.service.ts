import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface BpdeLigne {
  id?: number;
  projetId: number;
  projetNom?: string;
  designation: string;
  unite: string;
  prixUnitaire: number;
  ordre?: number;
}

@Injectable({ providedIn: 'root' })
export class BpdeLigneService {
  private apiUrl = `${environment.apiUrl}/bpde-lignes`;

  constructor(private http: HttpClient) {}

  getByProjet(projetId: number): Observable<BpdeLigne[]> {
    return this.http.get<BpdeLigne[]>(`${this.apiUrl}/projet/${projetId}`);
  }

  getById(id: number): Observable<BpdeLigne> {
    return this.http.get<BpdeLigne>(`${this.apiUrl}/${id}`);
  }

  create(dto: Partial<BpdeLigne>): Observable<BpdeLigne> {
    return this.http.post<BpdeLigne>(this.apiUrl, dto);
  }

  update(id: number, dto: Partial<BpdeLigne>): Observable<BpdeLigne> {
    return this.http.put<BpdeLigne>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
