import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { error, profile } from 'console';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaskPasswordPipe } from '../../pipes/mask-password.pipe';
import { Countries } from '../../datas/countries';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  userProfile: any = null;
  updatedUser: any = {};
  isEditing: boolean = false;
  countries: string[] =[];


  constructor(private userService: UserService, private router: Router){
    this.countries = Countries.countries;
  }

  ngOnInit(): void{
    this.loadUserProfile();
  }


  loadUserProfile(): void{
    this.userService.getUserProfile().subscribe({
      next:(profile) => {
        this.userProfile = profile;
        this.updatedUser = { 
          username: profile.username,
          email: profile.email,
          gender: profile.gender,
          country: profile.country
        };
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
    const userId = this.userProfile.id
    this.userService.deleteUser(userId).subscribe({
      next: () => {
        console.log('Kullanıcı başarıyla silindi');
        this.router.navigate(['/welcome'])
      },
      error: (error) => {
        console.error('Kullanıcı silinemedi: ', error);
      }
    })
  }
  saveUpdatedUser(): void {
    const userId = this.userProfile.id;
    let updatedUserData: any = {}; // Sadece değişen alanları tutacak

    // 🔹 Değişen alanları kontrol et ve updatedUserData içine ekle
    if (this.updatedUser.username !== this.userProfile.username) {
        updatedUserData.username = this.updatedUser.username;
    }
    if (this.updatedUser.email !== this.userProfile.email) {
        updatedUserData.email = this.updatedUser.email;
    }
    if (this.updatedUser.gender !== this.userProfile.gender) {
        updatedUserData.gender = this.updatedUser.gender;
    }
    if (this.updatedUser.country !== this.userProfile.country) {
        updatedUserData.country = this.updatedUser.country;
    }
    // 🔹 Eğer şifre alanı doldurulmuşsa backend'e gönder (aksi halde hiç gönderme)
    if (this.updatedUser.password && this.updatedUser.password.trim() !== "") {
        updatedUserData.password = this.updatedUser.password;
    }
    // Eğer güncellenecek herhangi bir alan yoksa işlemi iptal et
    if (Object.keys(updatedUserData).length === 0) {
        console.log("Hiçbir alan değişmedi, güncelleme iptal edildi.");
        this.isEditing = false;
        return;
    }
    this.userService.updateUser(userId, updatedUserData).subscribe({
      next: (response) => {
          console.log('Profil başarıyla güncellendi:', response);
          
          // Yeni token geldiyse localStorage'a kaydet
          if (response.token) {
              localStorage.setItem('token', response.token);
              window.location.reload();
              console.log("Yeni token kaydedildi.");
          }
            this.userProfile = { ...this.userProfile, ...updatedUserData }; // Güncellenen verileri userProfile'e işle
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
