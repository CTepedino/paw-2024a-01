import { Injectable } from '@angular/core';
import {environment} from "../../../enviroment/enviroment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable, tap} from "rxjs";
import {Index} from "../model";

@Injectable({
  providedIn: 'root'
})
export class ApiAuthService {

  private baseUrl = environment.apiURL;

  constructor(private http: HttpClient) { }

    login(email: string, password: string): Observable<Index> {
    const headers = new HttpHeaders({Authorization: 'Basic ' + btoa(`${email}:${password}`)});
    return this.http.get<Index>(this.baseUrl, {headers, observe: 'response'}).pipe(
        tap(response => {
            const jwt = response.headers.get('Authorization');
            const refreshToken = response.headers.get('X-Refresh-Token');
            if (jwt && refreshToken){
                this.storeTokens(jwt, refreshToken);
            }
        }),
        map(response => response.body!)
    );
    }

    logout(){
        localStorage.removeItem('jwt');
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
                this.storeTokens(jwt, newRefreshToken);
            }
        }),
        map(() => void 0)
    );
    }

    validateEmail(email: string, emailCode: string): Observable<Index> {
      return this.login(email, emailCode);
    }

    submitResetPasswordCode(email: string, resetCode: string): Observable<Index> {
      return this.login(email, resetCode)
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
    return localStorage.getItem('jwt');
    }

    getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
    }

    storeTokens(jwt: string, refreshToken: string){
      localStorage.setItem('jwt', jwt);
      localStorage.setItem('refreshToken', refreshToken);
    }



}
