import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/user';
 
  constructor(private http: HttpClient) {}

  register(username: string, password: string, email: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/register`, { username, password, email }, { responseType: 'text' });
  }


  login(username: string, password: string): Observable<string>{
    return this.http.post(`${this.baseUrl}/login`,  { username, password}, { responseType: 'text' });
  }
}
