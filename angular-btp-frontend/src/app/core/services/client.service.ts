import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { Client } from '../models/client.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = `${environment.apiUrl}/clients`;

  constructor(private http: HttpClient) { }

  /**
   * Retrieves a paginated list of all clients.
   */
  getAllClients(page: number, size: number, sort: string): Observable<Page<Client>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Client>>(this.apiUrl, { params });
  }

  /**
   * Retrieves a single client by its ID.
   * @param id The ID of the client to retrieve.
   */
  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  /**
   * Creates a new client.
   * @param client The client data to create. Use Partial<Client> to allow for sending objects without an ID.
   */
  createClient(client: Partial<Client>): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  /**
   * Updates an existing client.
   * @param id The ID of the client to update.
   * @param client The updated client data.
   */
  updateClient(id: number, client: Partial<Client>): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }

  /**
   * Deletes a client by its ID.
   * @param id The ID of the client to delete.
   */
  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
