import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http'; // HttpClient sağlayıcısı eklendi
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { MatToolbarModule } from '@angular/material/toolbar'; // Toolbar için
import { MatListModule } from '@angular/material/list'; // Mat-list-item için
import { MatButtonModule } from '@angular/material/button'; // Butonlar için
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(), // HttpClient sağlandı
    provideClientHydration(withEventReplay()),
    provideAnimationsAsync(),
    importProvidersFrom(MatButtonModule, MatListModule, MatToolbarModule)
  ]
};
