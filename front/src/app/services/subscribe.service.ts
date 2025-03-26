import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, of, tap, throwError } from "rxjs";
import { ApiService } from "./api.service";

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  private readonly BASE_URL = 'http://localhost:3001/api/subscription';

  constructor(private http: HttpClient, private apiService: ApiService) { }

  
  subscribeToTheme(themeId: number): Observable<any> {
    console.log('Requête abonnement envoyée au serveur, themeId :', themeId);
    return this.http.post(this.BASE_URL, { theme_id: themeId }, {
      headers: this.apiService.getAuthHeaders()
    })
    .pipe(
      tap(res => console.log('Réponse abonnement serveur :', res)),
      catchError(err => {
        console.error('Erreur HTTP abonnement serveur :', err);
        return throwError(() => err);
      })
    );
  }

  unsubscribeFromTheme(subscriptionId: number): Observable<any> {
    console.log('[unsubscribeFromTheme] SubscriptionId :', subscriptionId);
    return this.http.delete(`${this.BASE_URL}/${subscriptionId}`, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      tap(res => console.log('[unsubscribeFromTheme] Succès :', res)),
      catchError(err => {
        console.error('[unsubscribeFromTheme] Erreur serveur :', err);
        return throwError(() => err);
      })
    );
  }
  
  
  // subscribe.service.ts
  getUserSubscriptions(): Observable<{id: number, theme_id: number, user_id: number}[]> {
    return this.http.get<any>(this.BASE_URL, {
      headers: this.apiService.getAuthHeaders()
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