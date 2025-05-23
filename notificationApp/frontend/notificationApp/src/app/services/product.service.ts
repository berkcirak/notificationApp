import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productUrl = 'http://localhost:8080/product';



  constructor(private http: HttpClient) { }

  addProduct(product: any): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.post(`${this.productUrl}/add`, product, { headers });
  }

  getProducts(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.get(this.productUrl+'/list', { headers })
  }
  getRecommendedProducts(): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.get(this.productUrl+'/recommended', { headers })
  }
  getProductsByCommonCategory(categoryName: string): Observable<Product[]>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.get<Product[]>(this.productUrl+'/commoncategory', { headers });
  }
  deleteProduct(productId: number): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.delete(`${this.productUrl}/delete/${productId}`,{ headers });
  }
  searchProducts(query: string): Observable<Product[]>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.get<Product[]>(this.productUrl+'/search?query=${query}', { headers });
  }
  searchProductsByCategory(category: string): Observable<any[]>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    })
    return this.http.get<any[]>(this.productUrl +'/by-category', { headers, params: { category } });
  }
  getDiscountedProducts(): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
    return this.http.get<Product[]>(this.productUrl + '/discounted', { headers })
  }
}
