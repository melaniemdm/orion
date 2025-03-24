import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, of, tap, throwError } from "rxjs";

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    console.log('Token récupéré :', token);
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  subscribeToTheme(themeId: number): Observable<any> {
    console.log('Requête abonnement envoyée au serveur, themeId :', themeId);
    return this.http.post('/api/subscription', { theme_id: themeId }, {
      headers: this.getAuthHeaders()
    })
    .pipe(
      tap(res => console.log('Réponse abonnement serveur :', res)),
      catchError(err => {
        console.error('Erreur HTTP abonnement serveur :', err);
        return throwError(() => err);
      })
    );
  }

  unsubscribeFromTheme(themeId: number): Observable<any> {
    console.log('Requête désabonnement envoyée au serveur, themeId :', themeId);
    return this.http.delete(`/api/subscription/${themeId}`, {
      headers: this.getAuthHeaders()
    })
    .pipe(
      tap(res => console.log('Réponse désabonnement serveur :', res)),
      catchError(err => {
        console.error('Erreur HTTP désabonnement serveur :', err);
        return throwError(() => err);
      })
    );
  }
  // subscribe.service.ts
  getUserSubscriptions(): Observable<{id: number, theme_id: number, user_id: number}[]> {
    return this.http.get<any>('/api/subscription', {
      headers: this.getAuthHeaders()
    }).pipe(
      map(response => {
        console.log('Structure complète de la réponse:', response);
        // Vérifiez la structure exacte de votre réponse
        if (response && response.subscribe && Array.isArray(response.subscribe)) {
          return response.subscribe;
        } else {
          console.error('Structure inattendue de la réponse:', response);
          return [];
        }
      }),
      catchError(err => {
        console.error('Erreur récupération abonnements :', err);
        return of([]);
      })
    );
  }
  
  
  
}