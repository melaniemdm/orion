import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  map, Observable, tap } from 'rxjs';
import { User } from '../interfaces/user.interfaces';
import { ApiService } from './api.service';


@Injectable({
    providedIn: 'root'
})
export class UserService {

  
    private readonly USER_URL = 'http://localhost:3001/api/user';

    constructor(private httpClient: HttpClient, private apiService: ApiService) { }

    getUserById(id: number): Observable<User> {
      return this.httpClient.get<User>(`${this.USER_URL}/${id}`, {
        headers: this.apiService.getAuthHeaders()
      });
    }
  
    getAllUsers(): Observable<User[]> {
      return this.httpClient.get<any>(this.USER_URL, {
        headers: this.apiService.getAuthHeaders()
      }).pipe(
        map(resp => Array.isArray(resp.user) ? resp.user : [])
      );
    }
  
    updateUser(id: number, data: Partial<User>): Observable<User> {
      return this.httpClient.put<User>(`${this.USER_URL}/${id}`, data, {
        headers: this.apiService.getAuthHeaders()
      }).pipe(
        tap(updatedUser => {
          console.log('✅ Utilisateur mis à jour :', updatedUser);
        })
      );
    }
}
