import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, of, tap, throwError } from "rxjs";
import { ApiService } from "../interceptors/api.service";

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  private readonly BASE_URL: string;

  constructor(private http: HttpClient, private apiService: ApiService) {
    this.BASE_URL = this.apiService.getApiSubscription();
  }


  subscribeToTheme(themeId: number): Observable<any> {
   
    return this.http.post(this.BASE_URL, { theme_id: themeId }, {
      headers: this.apiService.getAuthHeaders()
    })
      .pipe(
        
        catchError(err => {
          console.error('Erreur HTTP abonnement serveur :', err);
          return throwError(() => err);
        })
      );
  }

  unsubscribeFromTheme(subscriptionId: number): Observable<any> {
   
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
  getUserSubscriptions(): Observable<{ id: number, theme_id: number, user_id: number }[]> {
    return this.http.get<any>(this.BASE_URL, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      map(response => {
        
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