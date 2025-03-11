import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import {  ArticleRequest, ArticleResponse } from '../interfaces/article.interfaces';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';


@Injectable({
  providedIn: 'root'
})
export class ArticleService {
    private readonly BASE_URL = 'http://localhost:3001/api/post';

    constructor(private http: HttpClient) {}
   // Méthode existante pour poster un article
   public registerArticle(request: ArticleRequest): Observable<AuthSuccess> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http
      .post<AuthSuccess>(this.BASE_URL, request, { headers })
      .pipe(catchError(this.handleError));
  }
 private handleError(error: any): Observable<never> {
    
    return throwError(() => new Error(error?.message || 'Server error'));
  }
  public getAllArticles(): Observable<ArticleRequest[]> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    // On définit un type correspondant à { post: ArticleRequest[] }
    

    return this.http
      .get<ArticleResponse>(this.BASE_URL, { headers })
      .pipe(
        // On récupère le tableau dans .post
        map(response => response.post),
        catchError(this.handleError)
      );
  }

  
}
