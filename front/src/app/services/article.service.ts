import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import {  ArticleRequest } from '../interfaces/article.interfaces';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';


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
}
