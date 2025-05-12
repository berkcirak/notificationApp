import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { response } from 'express';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-discountedproducts',
  imports: [CommonModule],
  templateUrl: './discountedproducts.component.html',
  styleUrl: './discountedproducts.component.css'
})
export class DiscountedproductsComponent implements OnInit{

  discountedProducts: any[] = [];
  
  constructor(private productService: ProductService){}
  
  ngOnInit(): void{
    this.productService.getDiscountedProducts().subscribe({
      next: (products) => {
        this.discountedProducts = products;
      },
      error: (err) => {
        console.error('İndirimli ürünler alınırken hata oluştu:', err);
      }
    });
  }
  
  goToProduct(link: string) {
  window.open(link, '_blank'); // Yeni sekmede açar
}
}
