import {Injectable} from "@angular/core";
import {QuestionService} from "../../../shared/services/question.service";
import {UserService} from "../../../shared/services/user.service";
import {BookService} from "../../../shared/services/book.service";
import {Book} from "../../../shared/model/book/book";
import {QuestionSearchQuery} from "../../../shared/model/question/questionSearchQuery";
import {catchError, concatMap, forkJoin, map, Observable, of} from "rxjs";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {QuestionWithData} from "../../../shared/model/question/questionWithData";
import {Question} from "../../../shared/model/question/question";
import {Answer} from "../../../shared/model/question/answer";
import {AuthService} from "../../../shared/services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class QuestionWithDataService {

  constructor(private questionService: QuestionService, private userService: UserService, private bookService: BookService, private authService: AuthService) {}

  books: Map<string, Book> = new Map<string, Book>;

    getAskedQuestions(page: number, size: number = 10){
      return this.authService.getLoggedUser().pipe(
          concatMap(user => this.getQuestions({
              questioner_id: user?.id,
              page: page,
              size: size
          }).pipe(
            concatMap(questionPage => this.fillWriterInfo(questionPage))
          ))
      );
    }

    getReceivedQuestions(page: number, size: number = 10, include_answered: boolean){
        return this.authService.getLoggedUser().pipe(
            concatMap(user => this.getQuestions({
                writer_id: user?.id,
                page: page,
                size: size,
                is_answered: include_answered? undefined : false
            }).pipe(
                concatMap(questionPage => this.fillQuestionerInfo(questionPage))
            ))
        );
    }

  private getQuestions(query: QuestionSearchQuery): Observable<PaginatedContent<QuestionWithData>> {
    return this.questionService.listQuestions(query).pipe(
        concatMap((questions) => {
          const questionRequests = questions.data.map(q => this.fillBookAndAnswerInfo(q));

          return forkJoin(questionRequests).pipe(
              map((questionWithData) => ({
                data: questionWithData,
                pagination: questions.pagination
              }))
          )
        })
    )
  }

  private fillWriterInfo(questions: PaginatedContent<QuestionWithData>): Observable<PaginatedContent<QuestionWithData>> {
    const writerRequests = questions.data.map(q => this.userService.getUser(q.writer!).pipe(
        map(writer => ({
          ...q,
          writerInfo: writer
        }))
    ));

    return forkJoin(writerRequests).pipe(
        map((questionsWithData) => ({
          data: questionsWithData,
          pagination: questions.pagination
        }))
    )
  }

  private fillQuestionerInfo(questions: PaginatedContent<QuestionWithData>): Observable<PaginatedContent<QuestionWithData>> {
    const questionerRequests = questions.data.map(q => this.userService.getUser(q.questioner!).pipe(
        map(user => ({
          ...q,
          questionerInfo: user
        }))
    ));

    return forkJoin(questionerRequests).pipe(
        map((questionsWithData) => ({
          data: questionsWithData,
          pagination: questions.pagination
        }))
    )
  }


  private fillBookAndAnswerInfo(question: Question): Observable<QuestionWithData> {
    const book$ = this.fetchBook(question.book!);
    const answer$ = this.fetchAnswer(question.self!);
    let forkQuery = {bookInfo: book$, answerInfo: answer$};

    return forkJoin({bookInfo: book$, answerInfo: answer$}).pipe(
        map(({bookInfo, answerInfo}) => ({
          ...question,
          bookInfo: bookInfo,
          answerInfo: answerInfo
        }))
    );
  }

  private fetchBook(bookUrl: string): Observable<Book>{
    if (this.books.has(bookUrl)){
      return of(this.books.get(bookUrl)!);
    }
    return this.bookService.getBook(bookUrl).pipe(
        map((book) => {
          this.books.set(bookUrl, book);
          return book;
        })
    );
  }

  private fetchAnswer(questionUrl: string): Observable<Answer | undefined>{
    return this.questionService.getAnswer(questionUrl).pipe(
        catchError(() => of(undefined))
    );
  }

}
