import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

const enhancedAppConfig = {
  ...appConfig,
  providers: [
    ...(appConfig.providers || []), // Mevcut sağlayıcıları korumak için
    provideHttpClient(withFetch()), provideAnimationsAsync(), // Fetch API desteği için
  ],
};

bootstrapApplication(AppComponent, enhancedAppConfig)
  .catch((err) => console.error(err));
