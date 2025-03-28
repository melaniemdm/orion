import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { Theme } from '../interfaces/theme.interfaces';
import { ApiService } from '../interceptors/api.service';



@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private readonly BASE_URL: string;

  constructor(private http: HttpClient, private apiService: ApiService) {
    this.BASE_URL = this.apiService.getApiSubject();
  }



  getThemes(): Observable<Theme[]> {
    return this.http.get<any>(this.BASE_URL, { headers: this.apiService.getAuthHeaders() })
      .pipe(
        map(response => {
          

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
