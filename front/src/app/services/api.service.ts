import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly BASE_URL = 'http://localhost:3001/api/';;


  constructor(private http: HttpClient) { }

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    //console.log('Token récupéré :', token);
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getApiArticles(): string {

    return this.BASE_URL + 'post';
  }
  getApiAuth(): string {

    return this.BASE_URL + 'auth';
  }
  getApiSubscription(): string {

    return this.BASE_URL + 'subscription';
  }
  getApiSubject(): string {

    return this.BASE_URL + 'subject';
  }

  getApiUser(): string {

    return this.BASE_URL + 'user';
  }
}

