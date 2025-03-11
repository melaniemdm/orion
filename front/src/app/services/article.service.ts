import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import {  ArticleRequest } from '../interfaces/article.interfaces';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';

export interface Article {
  id: number;
  title: string;
  content: string;
  author: string;
  date: string;
}

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
    private readonly BASE_URL = 'http://localhost:3001/api/post';

    constructor(private http: HttpClient) {}
   public registerArticle(request: ArticleRequest): Observable<AuthSuccess> {
     // Récupération du token depuis localStorage
     const token = localStorage.getItem('token') || '';

     // Construction des en-têtes
     const headers = new HttpHeaders({
       Authorization: `Bearer ${token}`,
       'Content-Type': 'application/json'
     });
      return this.http
        .post<AuthSuccess>(`${this.BASE_URL}`, request, { headers })
        .pipe(catchError(this.handleError));
    }
 private handleError(error: any): Observable<never> {
    
    return throwError(() => new Error(error?.message || 'Server error'));
  }

  getArticles(): Observable<Article[]> {
    const token = localStorage.getItem('token'); 
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
    return this.http.get<{ post: Article[] }>(this.BASE_URL, { headers }).pipe(
      tap(response => console.log("📡 Réponse brute du backend :", response)), 
      map(response => response.post || []), // ✅ Assure que nous avons bien un tableau d'articles
      catchError(this.handleError)
    );
  }
}
