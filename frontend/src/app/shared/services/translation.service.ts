import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TranslationService {
  private currentLang = new BehaviorSubject<string>('en');
  private translations = new BehaviorSubject<{ [key: string]: string }>({});

  constructor(private http: HttpClient) {}

  setLanguage(lang: string) {
    localStorage.setItem('lang', lang);
    this.currentLang.next(lang);
    this.loadTranslations(lang).subscribe((data) => {
      this.translations.next(data);
    });
  }

  get currentLanguage(): Observable<string> {
    return this.currentLang.asObservable();
  }

  get currentTranslations(): Observable<{ [key: string]: string }> {
    return this.translations.asObservable();
  }

  loadTranslations(lang: string): Observable<{ [key: string]: string }> {
    return this.http.get<{ [key: string]: string }>(`assets/i18n/${lang}.json`);
  }
}
