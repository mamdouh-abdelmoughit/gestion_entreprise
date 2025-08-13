import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Document } from '../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = '/api/documents';

  constructor(private http: HttpClient) { }

  getAllDocuments(page: number, size: number, sort: string): Observable<Page<Document>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Document>>(this.apiUrl, { params });
  }

  getDocumentById(id: number): Observable<Document> {
    return this.http.get<Document>(`${this.apiUrl}/${id}`);
  }

  createDocument(document: Partial<Document>): Observable<Document> {
    return this.http.post<Document>(this.apiUrl, document);
  }

  updateDocument(id: number, document: Partial<Document>): Observable<Document> {
    return this.http.put<Document>(`${this.apiUrl}/${id}`, document);
  }

  deleteDocument(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
