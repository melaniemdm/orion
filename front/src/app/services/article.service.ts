import { Injectable } from '@angular/core';
import { HttpClient, } from '@angular/common/http';
import { catchError, map, Observable, switchMap, throwError } from 'rxjs';
import { ArticleRequest, ArticleResponse, SingleArticleResponse } from '../interfaces/article.interfaces';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';
import { ApiService } from '../interceptors/api.service';
import { AuthService } from './auth.service';
import { User } from '../interfaces/user.interfaces';


@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private readonly BASE_URL: string;


  constructor(private http: HttpClient, private apiService: ApiService, private authService : AuthService) {
    this.BASE_URL = this.apiService.getApiArticles();
  }

  registerArticle(request: ArticleRequest): Observable<AuthSuccess> {
    return this.http.post<AuthSuccess>(this.BASE_URL, request, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  getAllArticles(): Observable<ArticleRequest[]> {
    return this.http.get<ArticleResponse>(this.BASE_URL, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      map(res => res.post),
      catchError(this.handleError)
    );
  }

  getArticleById(articleId: string): Observable<SingleArticleResponse> {
    return this.http.get<SingleArticleResponse>(`${this.BASE_URL}/${articleId}`, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  postComment(articleId: string, comment: string): Observable<Comment> {
    return this.authService.me().pipe(
      switchMap((user: User) => {
        const body = {
          article_id: articleId,
          commentary: comment,
          auteur_id: user.id // On récupère l'id du user connecté
        };
  
        return this.http.post<Comment>(
          `${this.BASE_URL}/${articleId}/comment`,
          body,
          { headers: this.apiService.getAuthHeaders() }
        );
      }),
      catchError(this.handleError)
    );
  }

  getComments(articleId: string): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.BASE_URL}/${articleId}/comment`, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  getUsers(): Observable<any[]> {
    const url = 'http://localhost:3001/api/user';
    return this.http.get<any[]>(url, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  getThemes(): Observable<{ subject: { id: number; name_theme: string }[] }> {
    const url = 'http://localhost:3001/api/subject';
    return this.http.get<{ subject: { id: number; name_theme: string }[] }>(url, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('Erreur API :', error);
    return throwError(() => new Error(error?.message || 'Erreur serveur'));
  }


}
