import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { error } from 'console';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone:true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  
  username: string = '';
  password: string = '';
  email: string = '';
  
  constructor(private authService: AuthService, private router: Router){}

  register(){
    this.authService.register(this.username, this.password, this.email).subscribe((res: any) => {
      if(res){
        localStorage.setItem('token', res);
        this.router.navigate(['/homepage']);
      }
    }, error => {
      console.error('register failed: ', error);
    })
  }


  goToLogin(){
    this.router.navigate(['/login']);
  }
}
