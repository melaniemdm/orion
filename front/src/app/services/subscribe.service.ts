import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  constructor(private http: HttpClient) { }

  subscribeToTheme(themeId: number): Observable<any> {
    return this.http.post('/api/subscription', { themeId });
  }

  unsubscribeFromTheme(themeId: number): Observable<any> {
    return this.http.delete(`/api/subscription/${themeId}`);
  }
}