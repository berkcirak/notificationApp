import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { error } from 'console';
import { Router } from '@angular/router';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { Countries } from '../../datas/countries';

@Component({
  selector: 'app-register',
  standalone:true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  
  user: User = new User;
  countries: string[] = [];

  constructor(private authService: AuthService, private router: Router){
    this.countries = Countries.countries;
  }


  register(registerForm: any){
    if(registerForm.invalid){
      alert('Lütfen tüm alanları eksiksiz doldurun.');
      return;
    }

    this.authService.register(this.user).subscribe((res: any) => {
      if(res){
        localStorage.setItem('token', res);
        this.router.navigate(['/homepage']).then(() => {
          window.location.reload();
        });
      }
    }, error => {
      console.error('register failed: ', error);
    })
  }


  goToLogin(){
    this.router.navigate(['/login']);
  }
}
