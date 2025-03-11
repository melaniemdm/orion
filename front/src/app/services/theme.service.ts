import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme } from '../interfaces/theme.interfaces';



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
