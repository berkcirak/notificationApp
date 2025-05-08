import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private baseUrl = 'http://localhost:8080/category';


  constructor(private http: HttpClient) { }

  getCategories(): Observable<any[]>{
     const headers = new HttpHeaders({
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        });
    return this.http.get<any[]>(`${this.baseUrl}/list`, { headers });
  }



}
