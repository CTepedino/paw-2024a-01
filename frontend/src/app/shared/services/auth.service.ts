import { Injectable } from '@angular/core';
import {environment} from "../../../enviroment/enviroment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, catchError, concatMap, map, Observable, of, switchMap, tap} from "rxjs";
import {Index} from "../model";
import {MediaTypes} from "../const/mediaTypes";
import {User} from "../model/user/user";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    private baseUrl = environment.apiURL;

    private loggedUserSubject = new BehaviorSubject<User | null | undefined>(undefined);
    private loggedUser$ = this.loggedUserSubject.asObservable();

    private remember: boolean = false;
    private firstGetUserCall = true;

    constructor(private http: HttpClient) {
        if (sessionStorage.getItem('refreshToken')){
            this.remember = false;
        } else if (localStorage.getItem('refreshToken')){
            this.remember = true;
        } else {
            this.loggedUserSubject.next(null);
        }
    }

    getLoggedUser(): Observable<User | null | undefined>{
        if (this.firstGetUserCall){
            this.firstGetUserCall = false;
            return this.http.get<Index>(this.baseUrl).pipe(
                concatMap(index => {
                    if (index.loggedUser){
                        return this.http.get<User>(index.loggedUser).pipe(
                            concatMap((user) => {
                                this.loggedUserSubject.next(user);
                                return this.loggedUser$;
                            })
                        )
                    }
                    return this.loggedUser$;
                })
            )
        }
        return this.loggedUser$;
    }

    getLoggedUserFromApi(): Observable<User | null>{
        return this.http.get<Index>(this.baseUrl).pipe(
            concatMap(index => {
                if (index.loggedUser){
                    return this.http.get<User>(index.loggedUser);
                }
                return of(null);
            }),
            catchError(() => of(null))
        )
    }

    resetLoggedUser() {
        const user = this.loggedUserSubject.value?.self;

        this.loggedUserSubject.next(undefined);
        this.http.get(user!).pipe(
            map(newUser => {
                this.loggedUserSubject.next(newUser);
            })
        ).subscribe();
    }

    getJwtToken(): string | null {
        return sessionStorage.getItem('jwt') || localStorage.getItem('jwt');
    }

    getRefreshToken(): string | null {
        return sessionStorage.getItem('refreshToken') || localStorage.getItem('refreshToken');
    }

    login(email: string, password: string, rememberMe: boolean): Observable<Index> {
        const headers = new HttpHeaders({Authorization: 'Basic ' + btoa(`${email}:${password}`)});
        return this.http.get<Index>(this.baseUrl, {headers: headers, observe: 'response'}).pipe(
            concatMap(response => {
                const jwt = response.headers.get('Authorization');
                const refreshToken = response.headers.get('X-Refresh-Token');

                if (jwt && refreshToken) {
                    this.setTokens(jwt, refreshToken, rememberMe);

                    return this.http.get(response.body?.loggedUser!).pipe(
                        map(user => {
                            this.loggedUserSubject.next(user);
                            return response.body!;
                        })
                    );
                }

                return of(response.body!);
            })
        );
    }

    logout() {
        sessionStorage.removeItem('jwt');
        sessionStorage.removeItem('refreshToken');
        localStorage.removeItem('jwt');
        localStorage.removeItem('refreshToken');
        this.loggedUserSubject.next(null);
    }

    private setTokens(jwt: string, refreshToken: string, remember: boolean = this.remember) {
        if (remember) {
            localStorage.setItem('jwt', jwt);
            localStorage.setItem('refreshToken', refreshToken);
        } else {
            sessionStorage.setItem('jwt', jwt);
            sessionStorage.setItem('refreshToken', refreshToken);
        }
        this.remember = remember;
    }

    private temporalLogout(): Tokens {
        const tokens = new Tokens(this.getJwtToken() ?? '', this.getRefreshToken() ?? '');
        sessionStorage.removeItem('jwt');
        sessionStorage.removeItem('refreshToken');
        localStorage.removeItem('jwt');
        localStorage.removeItem('refreshToken');
        return tokens;
    }

    refreshToken(): Observable<any> {
        if (!this.getRefreshToken()) {
            throw new Error('No refresh token');
        }
        const tokens = this.temporalLogout();

        const headers = new HttpHeaders({Authorization: tokens.refreshToken});

        return this.http.get<Index>(this.baseUrl, {headers: headers, observe: "response"}).pipe(
            concatMap(response => {
                const jwt = response.headers.get('Authorization');
                const refreshToken = response.headers.get('X-Refresh-Token');
                if (jwt && refreshToken) {
                    this.setTokens(jwt, refreshToken);
                    return this.http.get(response.body?.loggedUser!).pipe(
                        map(user => {
                            this.loggedUserSubject.next(user);
                            return response.body!;
                        })
                    );
                }
                return of(response.body!);
            })
        );
    }

    validateCode(id: string, code: string): Observable<Index> {
        return this.login(id, code, false);
    }

    sendResetPasswordCodeEmail(email: string): Observable<void> {
        return this.http.post(
            `${this.baseUrl}/reset-password-codes`,
            {email: email},
            {headers: {"Content-Type": MediaTypes.RESET_CODE}}
        ).pipe(
            map(() => void 0)
        )
    }

    resendVerificationCodeEmail(email: string): Observable<void> {
        return this.http.post(
            `${this.baseUrl}/email-validation-codes`,
            {email: email},
            {headers: {"Content-Type": MediaTypes.EMAIL_VALIDATION}}
        ).pipe(map(() => void 0))
    }

    verifyCredentials(email: string, password: string): Observable<boolean> {
        const tokens = this.temporalLogout();

        return this.http.get<Index>(this.baseUrl).pipe(
            map(index => {
                this.setTokens(tokens.jwt, tokens.refreshToken);
                return !!index.loggedUser;
            }),
            catchError(() => {
                this.setTokens(tokens.jwt, tokens.refreshToken);
                return of(false);
            })
        );
    }

}

class Tokens {
    jwt: string;
    refreshToken: string;

    constructor(jwt: string, refreshToken: string) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

}
