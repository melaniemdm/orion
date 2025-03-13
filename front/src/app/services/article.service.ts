import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import {  ArticleRequest, ArticleResponse, SingleArticleResponse } from '../interfaces/article.interfaces';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';


@Injectable({
  providedIn: 'root'
})
export class ArticleService {
    private readonly BASE_URL = 'http://localhost:3001/api/post';

    constructor(private http: HttpClient) {}
   
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
    //console.log('getAllArticles() - token lu dans localStorage:', token);
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    
    

    return this.http
      .get<ArticleResponse>(this.BASE_URL, { headers })
      .pipe(
        
        map(response => response.post),
        catchError(this.handleError)
      );
  }
  public getArticleById(articleId: string): Observable<SingleArticleResponse> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  
    return this.http
      .get<SingleArticleResponse>(`${this.BASE_URL}/${articleId}`, { headers })
      .pipe(catchError(this.handleError));
  }
  postComment(articleId: string, comment: string): Observable<Comment> {
    const token = localStorage.getItem('token');

    if (!token) {
        console.error("Erreur : Aucun token trouvé dans localStorage !");
        return throwError(() => new Error("Authentification requise"));
    }

    const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    });

    const body = {
      article_id: articleId, // Assure-toi d'envoyer l'ID de l'article
      commentary: comment,   // Le champ attendu par le backend
      auteur_id: "17" // À remplacer par l'ID de l'utilisateur connecté si nécessaire
  };
  console.log("🚀 Données envoyées au backend :", body);
    //console.log(`Envoi du commentaire à : ${this.BASE_URL}/${articleId}/comment avec token ${token}`);
  console.log(`Envoi du commentaire à : ${this.BASE_URL}/${articleId}/comment avec token ${token}`)
    return this.http.post<Comment>(`${this.BASE_URL}/${articleId}/comment`, body, { headers });
  }
  
}
