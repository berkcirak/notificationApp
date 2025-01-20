import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { MatListItem } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { MatNavList } from '@angular/material/list';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';
import { UserService } from './services/user.service';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, MatNavList, RouterModule, MatListItem, MatButtonModule, MatToolbarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  username: string | null = null;
  isAuthRoute:boolean = false;
  userProfile: any = null;
  title = 'notificationApp'; // title özelliği tanımlandı

  constructor(private router: Router, private authService: AuthService, private userService: UserService) {}
  ngOnInit(): void{
      this.loadUserProfile();
      // Rota değişikliklerini dinleyerek kontrol et
      this.router.events.subscribe(() => {
        const currentRoute = this.router.url;
        this.isAuthRoute = currentRoute.includes('/login') || currentRoute.includes('/register') || currentRoute.includes('/welcome'); 
      })
  }

  loadUserProfile(): void{
      this.userService.getUserProfile().subscribe({
        next:(profile) => {
          this.userProfile = profile;
        },
        error:(err) => {
          console.error('Profil bilgisi alınamadı:', err);
        }
      });
  }

  logout() {
    localStorage.removeItem('token'); // Token temizleme
    this.userProfile = null; // Kullanıcı profili sıfırlanıyor
    this.router.navigate(['/welcome']); // Welcome sayfasına yönlendirme
  }
  

  goToProfile() {
    this.router.navigate(['/profile']); // Profile yönlendirme
  }
}
