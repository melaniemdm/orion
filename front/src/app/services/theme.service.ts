import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Theme {
  id: number;
  name: string;
}
export interface ThemeResponse {
    subject?: Theme[]; // `?` permet de le rendre optionnel
  }

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private apiUrl = 'http://localhost:3001/api/subject';

  constructor(private http: HttpClient) { }

  getThemes(): Observable<Theme[]> {
    
    const token = localStorage.getItem('token'); 
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Theme[]>(this.apiUrl, { headers });
  }
}
