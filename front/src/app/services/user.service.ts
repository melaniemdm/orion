import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  map, Observable, tap } from 'rxjs';
import { User } from '../interfaces/user.interfaces';


@Injectable({
    providedIn: 'root'
})
export class UserService {

    private pathService = 'user';
    private readonly USER_URL = 'http://localhost:3001/api/user';
    constructor(private httpClient: HttpClient) { }

    public getUserById(id: number): Observable<User> {
        return this.httpClient.get<User>(`${this.pathService}/${id}`);
    }
    public getAllUsers(): Observable<User[]> {
        const token = localStorage.getItem('token') || '';
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        });
      
        return this.httpClient
          .get<any>(this.USER_URL, { headers })
          .pipe(
            tap((rawResponse) => {
             // console.log('getAllUsers() - Réponse brute du backend :', rawResponse);
            }),
            map((resp) => {
            
              //console.log('getAllUsers() - Après extraction du tableau :', resp.user);
            
              return resp.user; 
            }),
            
          );
      }
      public updateUser(id: number, data: Partial<User>): Observable<User> {
        const token = localStorage.getItem('token') || '';
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        });
      
        return this.httpClient
          .put<User>(`${this.USER_URL}/${id}`, data, { headers })
          .pipe(
            tap((updatedUser) => {
              console.log('Utilisateur mis à jour côté serveur :', updatedUser);
            })
          );
      }
      
   
}
