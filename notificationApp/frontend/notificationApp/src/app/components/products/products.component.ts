import { Component } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-products',
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent {

  products: any[] = [];
  selectedProduct: any = null;

  constructor(private productService: ProductService){}

  ngOnInit(){
    this.loadProducts();
  }

  loadProducts(){
    this.productService.getProducts().subscribe({
      next: (response) => {
        this.products=response;
      },
      error: (error) => {
        console.error('Ürünler yüklenemedi', error);
      }
    });
  }
  goToProduct(link: string){
    window.open(link, '_blank');
  }
  editProduct(product: any){
    this.selectedProduct = { ...product }
  }
  cancelEdit(){
    this.selectedProduct = null;
  }
  onUpdate(){
    if(!this.selectedProduct) return;

    this.productService.updateProduct(this.selectedProduct.id, this.selectedProduct).subscribe({
      next: () => {
        alert('Ürün başarıyla güncellendi!');
        this.selectedProduct = null;
        this.loadProducts();
      },
      error: (error) => {
        console.error('Ürün güncellenemedi: ', error);
        alert('Güncelleme sırasında bir hata oluştu.');
      }
    })
  }
  deleteProduct(productId: number){
    if(confirm('Bu ürünü silmek istediğinize emi misiniz?')){
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          alert('Ürün başarıyla silindi!');
          this.loadProducts();
        },
        error: (error) => {
          console.error('Ürün silinemedi:', error);
          alert('Silme işlemi sırasında hata oluştu');
        }
      });
    }
  }



}
