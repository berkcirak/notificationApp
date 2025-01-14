import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { MatListItem } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { MatNavList } from '@angular/material/list';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, MatNavList, RouterModule, MatListItem, MatButtonModule, MatToolbarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  username = 'Kullanıcı Adı';
  isAuthRoute = false;

  constructor(private router: Router) {
    // Rota değişikliklerini dinleyerek kontrol et

    this.router.events.subscribe(() => {
      const currentRoute = this.router.url;
      this.isAuthRoute = currentRoute.includes('/login') || currentRoute.includes('/register') || currentRoute.includes('/welcome'); 
    })
  }

  logout() {
    localStorage.removeItem('token'); // Token temizleme
    this.router.navigate(['/welcome']); // Login'e yönlendirme
  }

  goToProfile() {
    this.router.navigate(['/profile']); // Profile yönlendirme
  }
}
