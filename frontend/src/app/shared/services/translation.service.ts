import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private translations: { [key: string]: string } = {};
  private currentLang = new BehaviorSubject<string>('en');
  currentLang$ = this.currentLang.asObservable();

  constructor(private http: HttpClient) {}

  setLanguage(lang: string) {
    localStorage.setItem('lang', lang);
    this.currentLang.next(lang);
    this.loadTranslations(lang).subscribe();
  }

  loadTranslations(lang: string): Observable<{ [key: string]: string }> {
    return this.http.get<{ [key: string]: string }>(`/assets/i18n/${lang}.json`).pipe(
        tap(data => {
          this.translations = data;
          console.log('Traducciones cargadas:', data);
        })
    );
  }

  getTranslation(key: string): string {
    return this.translations[key] || key;
  }
}
