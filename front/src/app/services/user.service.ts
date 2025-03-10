import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interfaces';


@Injectable({
    providedIn: 'root'
})
export class UserService {

    private pathService = 'user';

    constructor(private httpClient: HttpClient) { }

    public getUserById(id: number): Observable<User> {
        return this.httpClient.get<User>(`${this.pathService}/${id}`);
    }
}
