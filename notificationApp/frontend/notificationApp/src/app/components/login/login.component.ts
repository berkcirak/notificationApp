import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { response } from 'express';
import { FormsModule } from '@angular/forms';
import { error } from 'console';
import { UserService } from '../../services/user.service';
@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  username: string = '';
  password: string = '';

  constructor( private router: Router, private authService: AuthService){}

  login() {
    this.authService.login(this.username, this.password).subscribe((res: any) => {
      // Eğer backend sadece token döndürüyorsa:
      if (res) {
        localStorage.setItem('token', res); // Token'ı kaydetme
        this.router.navigate(['/homepage']).then(() => {
          window.location.reload(); // Sayfayı yeniden yükleme
        });
      }
    }, error => {
      console.error('Login failed:', error); // Hata durumu
    });
  }
  goToRegister(){
    this.router.navigate(['/register'])
  }




}
