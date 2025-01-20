import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = 'http://localhost:8080/user'; 

  
  constructor(private http: HttpClient) { }


  getUserProfile(): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.get(this.userUrl+'/profile', { headers });
  }
  
  updateUser(userId: number, updatedUser: any): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.put(`${this.userUrl}/update/${userId}`, updatedUser,  { headers });
  }
  deleteUser(userId: number): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.delete(`${this.userUrl}/delete/${userId}`, { headers });
  }


}
