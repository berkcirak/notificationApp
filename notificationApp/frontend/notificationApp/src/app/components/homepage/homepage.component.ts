import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { CommonModule } from '@angular/common';
import { Categories } from '../../datas/categories';
import { FormsModule } from '@angular/forms';
import { MatListItem } from '@angular/material/list';
import { MatNavList } from '@angular/material/list';
import { CategoryService } from '../../services/category.service';

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
  mainCategories: any[] = [];


loadCategories(): void {
  this.categoryService.getCategories().subscribe({
    next: (allCategories) => {
      const grouped: any[] = [];

      const groupMap = [
        { key: 'fashionCategory', name: 'Moda', keywords: Categories.fashionCategory },
        { key: 'electronicsCategory', name: 'Elektronik', keywords: Categories.electronicsCategory },
        { key: 'cosmeticsCategory', name: 'Kozmetik', keywords: Categories.cosmeticsCategory },
        { key: 'hobbyCategory', name: 'Hobi', keywords: Categories.hobbyCategory },
        { key: 'householdCategory', name: 'Ev', keywords: Categories.householdCategory },
      ];

      for (const group of groupMap) {
        const subcategories = allCategories
          .filter(cat => group.keywords.includes(cat.name))
          .map(cat => cat.name)
          .sort();

        if (subcategories.length > 0) {
          grouped.push({
            key: group.key,
            name: group.name,
            values: subcategories
          });
        }
      }

      this.mainCategories = grouped;
    },
    error: (err) => console.error('Kategoriler alınamadı:', err)
  });
}
  selectCategory(category: string){
    this.selectedCategory = category;
    this.productService.searchProductsByCategory(category).subscribe({
      next: (response) => {
        this.filteredProducts = response;
      },
      error: (err) => {
        console.error("Kategoriye göre ürünler alınamadı: ",err);
      }
    });
  }
  filterProducts(){
    this.filteredProducts = this.recommendedProducts.filter(product => {
      const productCategory = (product.category?.name || '').toLowerCase();
      const selected = this.selectedCategory?.toLowerCase() || '';
      return productCategory.includes(selected);
    })
  }
  
  constructor(private productService: ProductService,
              private categoryService: CategoryService
  ){}
  ngOnInit(){
    this.loadRecommendedProducts();
    this.loadCategories();
  }
  onSearch(): void {
    const query = this.searchQuery.trim().toLowerCase();

    if(query === ''){
      this.filteredProducts = this.recommendedProducts;
    }else{
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
