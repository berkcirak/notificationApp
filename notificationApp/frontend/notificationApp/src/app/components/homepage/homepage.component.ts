import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { response } from 'express';
import { error } from 'console';
import { CommonModule } from '@angular/common';
import { Categories } from '../../datas/categories';
import { FormsModule } from '@angular/forms';
import { MatListItem } from '@angular/material/list';
import { MatNavList } from '@angular/material/list';

@Component({
  selector: 'app-homepage',
  imports: [CommonModule, FormsModule, MatListItem, MatNavList],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent {
  categories: string[] = [];  
  recommendedProducts: any[] = [];
  selectedCategory: string | null = null;
  filteredProducts: any[] = [];
  hoveredCategory: string | null = null;
  searchQuery: string = '';


  mainCategories = [
    { key: 'electronicsCategory', name: 'Elektronik', values: Categories.electronicsCategory.sort() },
    { key: 'fashionCategory', name: 'Moda', values: Categories.fashionCategory.sort() },
    { key: 'cosmeticsCategory', name: 'Kozmetik', values: Categories.cosmeticsCategory.sort() },
    { key: 'hobbyCategory', name: 'Hobi', values: Categories.hobbyCategory.sort() },
    { key: 'houseHoldCategory', name: 'Ev', values: Categories.householdCategory.sort() },
  ]

  selectCategory(category: string){
    this.selectedCategory = category;
    this.filterProducts();
  }
  filterProducts(){
    this.filteredProducts = this.recommendedProducts.filter(product => {
      const productCategory = (product.category?.name || '').toLowerCase();
      const selected = this.selectedCategory?.toLowerCase() || '';
      return productCategory.includes(selected);
    })
  }
  
  constructor(private productService: ProductService){}
//mapleme var mı? gereksiz mi? kontrol edilecek?
 /* mapCategory(product: any): string{
    const name = (product.productName || '').toLowerCase();
    const category = (product.category.name || '').toLowerCase();

    if(name.includes('telefon') || category.name.includes('telefon')) return 'Telefon';
    if(name.includes('kulaklık') || category.name.includes('kulaklık')) return 'kulaklık';
    if(name.includes('laptop') || category.name.includes('laptop')) return 'Laptop';
    if(name.includes('parfüm') || category.name.includes('parfüm')) return 'Parfüm';
    if(name.includes('ayakkabı') || category.name.includes('ayakkabı')) return 'Ayakkabı';
    if(name.includes('çanta') || category.name.includes('çanta')) return 'Çanta';
    if(name.includes('oyuncak') || category.name.includes('oyuncak')) return 'Oyuncak';
    if(name.includes('giyim') || category.name.includes('giyim')) return 'Giyim';
    if(name.includes('akıllı saat') || category.name.includes('akıllı saat')) return 'Akıllı Saat';

    return 'Diğer Göz Atabileceğiniz Ürünler';
  }
*/
  ngOnInit(){
    this.loadRecommendedProducts();
  }
  onSearch(): void {
    const query = this.searchQuery.trim().toLowerCase();
  
    if (query === '') {
      this.filteredProducts = this.recommendedProducts;
    } else {
      this.filteredProducts = this.recommendedProducts.filter(product =>
        product.productName?.toLowerCase().includes(query) ||
        product.category?.name?.toLowerCase().includes(query)
      );
    }
  }
  
  
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
