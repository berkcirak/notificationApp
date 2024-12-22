import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { MatListItem } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { MatNavList } from '@angular/material/list';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatNavList, RouterModule, MatListItem, MatButtonModule, MatToolbarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  username = 'Kullanıcı Adı'; // Bu backend'den alınabilir

  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('token'); // Token'ı temizle
    this.router.navigate(['/login']); // Login sayfasına yönlendir
  }

  goToProfile() {
    this.router.navigate(['/profile']); // Profile sayfasına yönlendir
  }
}
