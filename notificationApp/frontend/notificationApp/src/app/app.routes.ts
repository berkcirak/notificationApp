import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { ProductsComponent } from './components/products/products.component';
import { AddproductComponent } from './components/addproduct/addproduct.component';
import { ProfileComponent } from './components/profile/profile.component';
import { DiscountedproductsComponent } from './components/discountedproducts/discountedproducts.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'homepage', component: HomepageComponent },
  { path: 'myproducts', component: ProductsComponent },
  { path: 'addproduct', component: AddproductComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'discounted', component: DiscountedproductsComponent },
  { path: '', redirectTo: 'welcome', pathMatch: 'full' } // Varsayılan yönlendirme
];
