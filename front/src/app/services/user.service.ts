import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { User } from '../interfaces/user.interfaces';
import { ApiService } from '../interceptors/api.service';


@Injectable({
  providedIn: 'root'
})
export class UserService {


  private readonly BASE_URL: string;

  constructor(private httpClient: HttpClient, private apiService: ApiService) {
    this.BASE_URL = this.apiService.getApiUser();
  }

  getUserById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${this.BASE_URL}/${id}`, {
      headers: this.apiService.getAuthHeaders()
    });
  }

  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<any>(this.BASE_URL, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      map(resp => Array.isArray(resp.user) ? resp.user : [])
    );
  }

  updateUser(id: number, data: Partial<User>): Observable<User> {
    return this.httpClient.put<User>(`${this.BASE_URL}/${id}`, data, {
      headers: this.apiService.getAuthHeaders()
    })
  }
}
