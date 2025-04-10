import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthSuccess } from '../interfaces/authAcces.interfaces';
import { LoginRequest } from '../interfaces/login.interfaces';
import { RegisterRequest } from '../interfaces/registerRequest.interfaces';
import { User } from '../interfaces/user.interfaces';
import { ApiService } from '../interceptors/api.service';
import { AbstractControl, ValidationErrors } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL: string;

  constructor(
    private http: HttpClient,
    private apiService: ApiService, 
  ) {
    this.BASE_URL = this.apiService.getApiAuth();
  }

  register(request: RegisterRequest): Observable<AuthSuccess> {
    return this.http.post<AuthSuccess>(`${this.BASE_URL}/register`, request)
      .pipe(catchError(this.handleError));
  }

  login(request: LoginRequest): Observable<AuthSuccess> {
    return this.http.post<AuthSuccess>(`${this.BASE_URL}/login`, request)
      .pipe(catchError(this.handleError));
  }

  me(): Observable<User> {
    return this.http.get<User>(`${this.BASE_URL}/me`, {
      headers: this.apiService.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    return throwError(() => new Error(error?.message || 'Erreur serveur'));
  }

  getToken(): string {
    return localStorage.getItem('token') || '';
  }

  // Personnalisation du mot de passe
 passwordValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value || '';

  const hasUpperCase = /[A-Z]/.test(value);
  const hasLowerCase = /[a-z]/.test(value);
  const hasNumber = /\d/.test(value);
  const hasSpecialChar = /[^A-Za-z0-9]/.test(value);
  const hasMinLength = value.length >= 8;

  const isValid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && hasMinLength;

  return isValid
    ? null
    : {
        passwordInvalid: true
      };

}
}
