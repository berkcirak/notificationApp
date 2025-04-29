import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { response } from 'express';
import { error } from 'console';
import { CommonModule } from '@angular/common';
import { Categories } from '../../datas/categories';
@Component({
  selector: 'app-homepage',
  imports: [CommonModule],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent {
  categories: string[] = [];
  
  recommendedProducts: any[] = [];
  groupedProducts: {[key: string]: any[] } = {};
  selectedCategory: string | null = null;
  filteredProducts: any[] = [];
  
  selectCategory(category: string){
    this.selectedCategory = category;
    this.filterProductsByCategory();
  }
  filterProductsByCategory(){
    if(this.selectedCategory){
      this.filteredProducts = this.recommendedProducts.filter(product => {
        const categoryName = (product.category?.name || '').toLowerCase();
        return categoryName.includes(this.selectedCategory?.toLowerCase());
      });
    }else{
      this.filteredProducts = this.recommendedProducts;
    }
  }

  constructor(private productService: ProductService){
    this.categories = Categories.categories;
  }
  mapCategory(product: any): string{
    const name = (product.productName || '').toLowerCase();
    const category = (product.category.name || '').toLowerCase();

    if(name.includes('telefon') || category.name.includes('telefon')) return 'Telefon';
    if(name.includes('kulaklık') || category.name.includes('kulaklık')) return 'kulaklık';
    if(name.includes('bilgisayar') || category.name.includes('bilgisayar')) return 'bilgisayar';
    if(name.includes('ütü') || category.name.includes('ev')) return 'Ev';
    if(name.includes('parfüm') || category.name.includes('parfüm')) return 'Parfüm';
    if(name.includes('ayakkabı') || category.name.includes('ayakkabı')) return 'Ayakkabı';
    if(name.includes('çanta') || category.name.includes('çanta')) return 'Çanta';
    if(name.includes('oyuncak') || category.name.includes('oyuncak')) return 'Oyuncak';
    if(name.includes('giyim') || category.name.includes('giyim')) return 'Giyim';
    return 'Diğer Göz Atabileceğiniz Ürünler';
  }

  ngOnInit(){
    this.loadRecommendedProducts();
  }
/*
  loadRecommendedProducts(){
    this.productService.getRecommendedProducts().subscribe({
      next:(response) => {
        this.recommendedProducts = response;
        this.groupedProducts = {};
        this.recommendedProducts.forEach(product => {
          const mappedCategory = this.mapCategory(product);

          if(!this.groupedProducts[mappedCategory]){
            this.groupedProducts[mappedCategory] = [];
          }
          this.groupedProducts[mappedCategory].push(product);
        });
        this.filteredProducts = response;
      },
      error: (error) => {
        console.error('Ürünler yüklenemedi', error);
      }
    });
  } 
*/

  
  loadRecommendedProducts(){
    this.productService.getRecommendedProducts().subscribe({
      next:(response) => {
        this.recommendedProducts = response;
        this.filteredProducts = response;
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
