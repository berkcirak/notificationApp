import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { response } from 'express';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';


@Component({
  selector: 'app-addproduct',
  imports: [FormsModule],
  templateUrl: './addproduct.component.html',
  styleUrl: './addproduct.component.css'
})
export class AddproductComponent {

  product = {
    link: ''
  };

  constructor(private authService: AuthService, private productService: ProductService){

  }

  onSubmit(){
    this.productService.addProduct(this.product).subscribe({
      next: (response) => {
        alert('Ürün başarıyla eklendi!');
        this.product ={ link: '' };
      },
      error: (err) => {
        console.error(err);
        if(err.status === 401){
          alert('Yetkilendirme hatası. Lütfen tekrar giriş yapın.');
        }else{
          alert('Bir hata oluştu. Lütfen tekrar deneyin.');
        }
      },
    });
  }
}
