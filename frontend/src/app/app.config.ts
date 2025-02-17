import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import {provideRouter, withComponentInputBinding} from '@angular/router';

import { routes } from './app.routes';
import {HttpClient, provideHttpClient, withFetch, withInterceptors, withInterceptorsFromDi} from "@angular/common/http";
import {authInterceptor} from "./shared/interceptors/auth.interceptor";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";

export function createTranslateLoader(http: HttpClient){
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export const appConfig: ApplicationConfig = {
  providers: [
      provideZoneChangeDetection({ eventCoalescing: true }),
      importProvidersFrom([
          TranslateModule.forRoot({
              loader: {
                  provide: TranslateLoader,
                  useFactory: (createTranslateLoader),
                  deps: [HttpClient]
              },
              defaultLanguage:'en',
          })
      ]),
      provideRouter(routes, withComponentInputBinding()),
      provideHttpClient(
          withInterceptors([authInterceptor])
      ), provideAnimationsAsync(),
      provideHttpClient(withInterceptorsFromDi()),
  ]
};
