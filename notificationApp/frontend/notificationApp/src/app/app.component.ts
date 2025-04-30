import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
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
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  username: string | null = null;
  isAuthRoute: boolean = false;
  userProfile: any = null;
  title = 'notificationApp';

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    @Inject(PLATFORM_ID) private platformId: Object // Tarayıcı kontrolü için PLATFORM_ID inject ediyoruz
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      // Sadece tarayıcı ortamında çalışır
      this.loadUserProfile();
    }
    this.router.events.subscribe(() => {
      const currentRoute = this.router.url;
      this.isAuthRoute =
        currentRoute.includes('/login') ||
        currentRoute.includes('/register') ||
        currentRoute.includes('/welcome');
    });
  }
  goToHomepage(): void{
    this.router.navigate(['/homepage']);
    window.location.reload();
  }

  loadUserProfile(): void {
    if (isPlatformBrowser(this.platformId)) {
      // localStorage kontrolü sadece tarayıcı ortamında yapılır
      this.userService.getUserProfile().subscribe({
        next: (profile) => {
          this.userProfile = profile;
        },
        error: (err) => {
          console.error('Profil bilgisi alınamadı:', err);
        }
      });
    }
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token'); // Token temizleme
    }
    this.userProfile = null; // Kullanıcı profili sıfırlanıyor
    this.router.navigate(['/welcome']); // Welcome sayfasına yönlendirme
  }

  goToProfile() {
    this.router.navigate(['/profile']); // Profile yönlendirme
  }
}
