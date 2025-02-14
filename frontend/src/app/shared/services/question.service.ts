import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {QuestionSearchQuery} from "../model/question/questionSearchQuery";
import {map, Observable} from "rxjs";
import {Question} from "../model/question/question";
import {Answer} from "../model/question/answer";
import {MediaTypes} from "../const/mediaTypes";
import {PaginatedContent, setPagination} from "../model/paginatedContent";

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private apiUrl = `${environment.apiURL}/questions`;

  constructor(private http: HttpClient) { }

  listQuestions(query: QuestionSearchQuery): Observable<PaginatedContent<Question>>{
    if (!query.size){
      query.size = 10;
    }

    let params = new HttpParams();

    Object.entries(query).forEach(([name, value]) => {
        if (value !== null && value !== undefined){
          params = params.append(name, value);
        }
    });

    return this.http.get<Question[]>(this.apiUrl, {params: params, observe: 'response'}).pipe(
        map((response) => setPagination(response, query.size!))
    );
  }

  postQuestion(question: Question): Observable<void>{
    return this.http.post<void>(
        this.apiUrl,
        question,
        {headers: {"Content-Type": MediaTypes.QUESTION}}
    );
  }

  getQuestion(questionUrl: string): Observable<Question> {
    return this.http.get<Question>(questionUrl);
  }

  getAnswer(questionUrl: string): Observable<Answer> {
    return this.http.get<Answer>(`${questionUrl}/answer`);
  }

  putAnswer(questionUrl: string, answer: Answer): Observable<void> {
    return this.http.put<void>(
        `${questionUrl}/answer`,
        answer,
        {headers: {"Content-Type": MediaTypes.ANSWER}}
    );
  }
}
