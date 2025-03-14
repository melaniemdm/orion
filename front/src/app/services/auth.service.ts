import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';
import { LoginRequest } from '../interfaces/login.interfaces';
import { RegisterRequest } from '../interfaces/registerRequest.interfaces';
import { User } from '../interfaces/user.interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = 'http://localhost:3001/api/auth';

  constructor(private http: HttpClient) {}

  public register(request: RegisterRequest): Observable<AuthSuccess> {
    return this.http
      .post<AuthSuccess>(`${this.BASE_URL}/register`, request)
      .pipe(catchError(this.handleError));
  }

  public login(request: LoginRequest): Observable<AuthSuccess> {
    return this.http
      .post<AuthSuccess>(`${this.BASE_URL}/login`, request)
      .pipe(catchError(this.handleError));
  }
/**
   * me :
   * Récupère les informations de l'utilisateur actuellement connecté
   * en se basant sur le token stocké dans le localStorage.
   * @returns Un Observable<User> contenant les informations de l'utilisateur connecté.
   */
  public me(): Observable<User> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    //console.log("TOKEN envoyé à l'API:", token);

    return this.http
      .get<User>(`${this.BASE_URL}/me`, { headers })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    // Possibilité d'ajouter un logging plus complet ici
    return throwError(() => new Error(error?.message || 'Server error'));
  }
}
