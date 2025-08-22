import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Document } from '../models/document.model';
import { environment } from '../../../environments/environment'; // IDE might flag this


@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = `${environment.apiUrl}/documents`;

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

  createDocument(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/upload`, formData, {
      headers: {
        // 🚨 DO NOT set Content-Type manually!
        // Let the browser set `multipart/form-data` with boundary
      }
    });
  }


  updateDocument(id: number, document: Partial<Document>): Observable<Document> {
    return this.http.put<Document>(`${this.apiUrl}/${id}`, document);
  }

  deleteDocument(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
// In document.service.ts
  viewDocument(id: number): Observable<Blob> {
      return this.http.get(`${this.apiUrl}/${id}/preview`, { // Changed from /view/:id
          responseType: 'blob'
      });
  }

  downloadDocument(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/download`, {
      responseType: 'blob' // Force download
    });
  }

}
