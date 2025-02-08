import { Injectable } from '@angular/core';
import {environment} from "../../../enviroment/enviroment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable, tap} from "rxjs";
import {Index} from "../model";

@Injectable({
  providedIn: 'root'
})
export class ApiAuthService {

  private apiUrl = environment.apiURL;

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<Index> {
    const headers = new HttpHeaders({Authorization: 'Basic ' + btoa(`${username}:${password}`)});
    return this.http.get<Index>(this.apiUrl, {headers, observe: 'response'}).pipe(
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

  refreshToken(): Observable<void> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken){
      throw new Error('No refresh token available');
    }
    const headers = new HttpHeaders({Authorization: refreshToken});

    return this.http.get<void>(this.apiUrl, {headers, observe: "response"}).pipe(
        tap(response => {
            const jwt = response.headers.get('Authorization');
            const newRefreshToken = response.headers.get('X-Refresh-Token');
            if (jwt && refreshToken){
                this.storeTokens(jwt, refreshToken);
            }
        }),
        map(() => void 0)
    );

  }

  logout(){
    localStorage.removeItem('jwt');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userUrl');
  }

  getJwtToken(): string | null {
    return localStorage.getItem('jwt');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  getUserUrl(): string | null {
      return localStorage.getItem('userUrl');
  }

  storeTokens(jwt: string, refreshToken: string){
      localStorage.setItem('jwt', jwt);
      localStorage.setItem('refreshToken', refreshToken);
  }

}
