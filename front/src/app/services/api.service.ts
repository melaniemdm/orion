import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
 
    constructor(private http: HttpClient) {}

getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    //console.log('Token récupéré :', token);
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }


}