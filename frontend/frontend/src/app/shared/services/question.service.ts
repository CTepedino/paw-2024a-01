import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {QuestionSearchQuery} from "../model/question/questionSearchQuery";
import {Observable} from "rxjs";
import {Question} from "../model/question/question";
import {Answer} from "../model/question/answer";

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private apiUrl = `${environment.apiURL}/questions`;

  constructor(private http: HttpClient) { }

  listQuestions(query: QuestionSearchQuery): Observable<Question[]>{
    const params = new HttpParams();

    Object.entries(query).forEach(([name, value]) => {
        if (value !== null && value !== undefined){
          params.append(name, value);
        }
    });

    return this.http.get<Question[]>(this.apiUrl, {params: params});
  }

  postQuestion(question: Question){
    this.http.post(
        this.apiUrl,
        question,
        {headers: {"Content-Type": "application/vnd.questions.v1+json"}}
    );
  }

  getQuestion(questionUrl: string): Observable<Question> {
    return this.http.get<Question>(questionUrl);
  }

  getAnswer(questionUrl: string): Observable<Answer> {
    return this.http.get<Answer>(`${questionUrl}/answer`);
  }

  putAnswer(questionUrl: string, answer: Answer) {
    this.http.put<Answer>(
        `${questionUrl}/answer`,
        answer,
        {headers: {"Content-Type": "application/vnd.questions.answers.v1+json"}}
    );
  }
}
