import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private translations: { [key: string]: { [key: string]: string } } = {
    en: { WELCOME: 'Welcome', LOGIN: 'Login', SIGNUP: 'Sign Up' },
    es: { WELCOME: 'Bienvenido', LOGIN: 'Iniciar sesión', SIGNUP: 'Registrarse' }
  };

  private currentLangSubject = new BehaviorSubject<string>('en');
  currentLang$ = this.currentLangSubject.asObservable();

  constructor() {
    const savedLang = localStorage.getItem('lang') || navigator.language.split('-')[0] || 'en';
    this.setLanguage(savedLang);
  }

  setLanguage(lang: string) {
    if (!this.translations[lang]) lang = 'en'; // Si el idioma no existe, usar inglés por defecto
    localStorage.setItem('lang', lang);
    this.currentLangSubject.next(lang);
  }

  getTranslation(key: string): string {
    const lang = this.currentLangSubject.getValue();
    return this.translations[lang]?.[key] || key;
  }
}
