import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { error, profile } from 'console';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaskPasswordPipe } from '../../pipes/mask-password.pipe';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule, MaskPasswordPipe],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  userProfile: any = null;
  updatedUser: any = {};
  isEditing: boolean = false;

  constructor(private userService: UserService){}

  ngOnInit(): void{
    this.loadUserProfile();
  }


  loadUserProfile(): void{
    this.userService.getUserProfile().subscribe({
      next:(profile) => {
        this.userProfile = profile;
        this.updatedUser = { ...profile}
      },
      error: (err) => {
        console.error('Profil bilgisi alınamadı:', err);
      }
    });
  }

  editUser(): void {
    this.isEditing = true;
  }

  deleteUser(): void{
    const userId = this.userProfile.id;
    this.userService.deleteUser(userId).subscribe({
      next: () => {
        console.log('Kullanıcı başarıyla silindi');
      },
      error: (error) => {
        console.error('Kullanıcı silinemedi: ', error);
      }
    })
  }
  saveUpdatedUser(): void{
    const userId = this.userProfile.id;
    this.userService.updateUser(userId, this.updatedUser).subscribe({
      next: (updatedProfile) => {
        console.log('Profil başarıyla güncellendi: ', updatedProfile);
        this.userProfile = updatedProfile;
        this.isEditing = false;
      },
      error: (error) => {
        console.error('Profil güncellemesi başarısız: ', error);
      }
    });
  }
  cancelEdit(): void {
    this.isEditing = false;
    this.updatedUser = { ...this.userProfile };
  }

}
