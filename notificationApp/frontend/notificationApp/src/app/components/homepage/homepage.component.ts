import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { response } from 'express';
import { error } from 'console';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-homepage',
  imports: [CommonModule],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent {
  
  recommendedProducts: any[] = [];

  constructor(private productService: ProductService){}

  ngOnInit(){
    this.loadRecommendedProducts();
  }
  loadRecommendedProducts(){
    this.productService.getRecommendedProducts().subscribe({
      next:(response) => {
        this.recommendedProducts = response;
      },
    error: (error) => {
      console.error('Ürünler yüklenemedi', error);
    }
  });
}
goToProduct(link: string) {
  window.open(link, '_blank'); // Yeni sekmede açar
}





}
