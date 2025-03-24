import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { Theme } from '../interfaces/theme.interfaces';



@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private apiUrl = 'http://localhost:3001/api/subject';

  constructor(private http: HttpClient) { }
  
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    console.log('Token récupéré :', token);
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }
  // vérification 

  getThemes(): Observable<Theme[]> {
    return this.http.get<any>(this.apiUrl, { headers: this.getAuthHeaders() })
      .pipe(
        map(response => {
          console.log('Réponse API thèmes brute:', response);
  
          if (response && Array.isArray(response.subject)) {
            // Retourne explicitement response.subject
            return response.subject;
          } else {
            console.warn('Structure inattendue reçue pour thèmes:', response);
            return [];
          }
        }),
        catchError(err => {
          console.error('Erreur récupération thèmes :', err);
          return of([]);
        })
      );
  }
  

}
