import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-welcome',
  imports: [CommonModule, FormsModule],
  standalone:true,
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css'
})
export class WelcomeComponent {

  username: string = '';
  password: string = '';


  constructor(private authService: AuthService, private router: Router){

  }

  login() {
    this.authService.login(this.username, this.password).subscribe((res: any) => {
      // Eğer backend sadece token döndürüyorsa:
      if (res) {
        localStorage.setItem('token', res); // Token'ı kaydetme
        this.router.navigate(['/homepage']).then(() =>{
          window.location.reload();
        }); // Login başarılı olursa yönlendirme
      }
    }, error => {
      console.error('Login failed:', error); // Hata durumu
    });
  }

  goToRegister(){
    this.router.navigate(['/register'])
  }



}
