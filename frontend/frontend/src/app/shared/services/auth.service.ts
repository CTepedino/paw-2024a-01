import { Injectable } from '@angular/core';
import {environment} from "../../../enviroment/enviroment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable, tap} from "rxjs";
import {Index} from "../model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = environment.apiURL;

  constructor(private http: HttpClient) { }

    login(email: string, password: string, rememberMe: boolean = false): Observable<Index> {
    const headers = new HttpHeaders({Authorization: 'Basic ' + btoa(`${email}:${password}`)});
    return this.http.get<Index>(this.baseUrl, {headers, observe: 'response'}).pipe(
        tap(response => {
            const jwt = response.headers.get('Authorization');
            const refreshToken = response.headers.get('X-Refresh-Token');
            if (jwt && refreshToken){
                this.storeTokens(jwt, refreshToken, false);
            }
        }),
        map(response => response.body!)
    );
    }

    logout(){
        sessionStorage.removeItem('jwt');
        localStorage.removeItem('jwt');
        sessionStorage.removeItem('refreshToken');
        localStorage.removeItem('refreshToken');
    }

    refreshToken(): Observable<void> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken){
      throw new Error('No refresh token available');
    }
    const headers = new HttpHeaders({Authorization: refreshToken});

    return this.http.get<void>(this.baseUrl, {headers, observe: "response"}).pipe(
        tap(response => {
            const jwt = response.headers.get('Authorization');
            const newRefreshToken = response.headers.get('X-Refresh-Token');
            if (jwt && newRefreshToken){
                this.updateTokens(jwt, newRefreshToken);
            }
        }),
        map(() => void 0)
    );
    }

    validateEmail(email: string, emailCode: string): Observable<Index> {
      return this.login(email, emailCode, false);
    }

    submitResetPasswordCode(email: string, resetCode: string): Observable<Index> {
      return this.login(email, resetCode, false)
    }

    sendResetPasswordCodeEmail(email: string): Observable<void> {
      return this.http.post(
          `${this.baseUrl}/reset-password-codes`,
          {email: email},
          {headers: {"Content-Type": "application/vnd.reset-code.v1+json"}}
      ).pipe(map(() => void 0))
    }

    resendVerificationCodeEmail(email: string): Observable<void> {
        return this.http.post(
            `${this.baseUrl}/email-validation-codes`,
            {email: email},
            {headers: {"Content-Type": "application/vnd.validation-code.v1+json"}}
        ).pipe(map(() => void 0))
    }

    getJwtToken(): string | null {
    return sessionStorage.getItem('jwt') || localStorage.getItem('jwt');
    }

    getRefreshToken(): string | null {
    return sessionStorage.getItem('refreshToken') || localStorage.getItem('refreshToken');
    }

    storeTokens(jwt: string, refreshToken: string, rememberMe: boolean){
     if (rememberMe) {
          localStorage.setItem('jwt', jwt);
          localStorage.setItem('refreshToken', refreshToken);
      } else {
          sessionStorage.setItem('jwt', jwt);
          sessionStorage.setItem('refreshToken', refreshToken);
      }
    }

    updateTokens(jwt: string, refreshToken: string){
      this.storeTokens(jwt, refreshToken, localStorage.getItem('jwt') !== null)
    }

}
