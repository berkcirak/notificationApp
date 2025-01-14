import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { request } from 'node:http';
@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token'); //localstorage'da saklanan token'ı aldık
    if (token) { // token varsa
      const cloned = req.clone({  //cloned'a request'i klonluyoruz 
        headers: req.headers.set('Authorization', `Bearer ${token}`) 
      }); //request authorization'a bearer token'ı aldık
      return next.handle(cloned); //token ile beraber sunucuya istek göderilir
    } else {
      return next.handle(req); //token yoksa orijinal istek gönderilir
    }
  
  }
}
