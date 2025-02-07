import { Injectable } from '@angular/core';
import {environment} from "../../enviroment/enviroment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiAuthService {

  private apiUrl = `${environment.apiURL}/users`;

  constructor(private http: HttpClient) { }

  login(username: string, password: string){
    const headers = new HttpHeaders({Authorization: 'Basic ' + btoa(`${username}:${password}`)});

    return this.http.get(this.apiUrl, {headers, observe: 'response'}).pipe(
        tap(response => {
            const jwt = response.headers.get('Authorization');
            const refreshToken = response.headers.get('X-Refresh-Token');
            if (jwt && refreshToken){
                this.storeTokens(jwt, refreshToken);
            }
        })
    )
  }

  refreshToken(): Observable<void> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken){
      throw new Error('No refresh token available');
    }
    const headers = new HttpHeaders({Authorization: `Bearer ${refreshToken}`});

    return this.http.get(this.apiUrl, {headers, observe: "response"})
  }

  logout(){
    localStorage.removeItem('jwt');
    localStorage.removeItem('refreshToken');
  }

  getJwtToken(): string | null {
    return localStorage.getItem('jwt');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  private storeTokens(jwt: string, refreshToken: string){
    localStorage.setItem('jwt', jwt);
    localStorage.setItem('refreshToken', refreshToken);
  }

}
