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
  user ={
    username: '',
    email: '',
    password: ''
  };
  constructor(private authService: AuthService, private router: Router){}

  registerUser(){
    this.authService.register(this.user).subscribe({
      next: (response) => {
        alert('Registration successful!');
        console.log(response);
      },
      error: (error) => {
        alert('Registration failed!');
        console.error(error);
      }
    });
    this.router.navigate(['/homepage'])
  }
  goToLogin(){
    this.router.navigate(['/login']);
  }
}
