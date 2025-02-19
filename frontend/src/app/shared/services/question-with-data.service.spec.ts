import { TestBed } from '@angular/core/testing';

import { QuestionWithDataService } from './question-with-data.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {QuestionService} from "./question.service";
import {UserService} from "./user.service";
import {BookService} from "./book.service";
import {AuthService} from "./auth.service";
import {of} from "rxjs";
import {Question} from "../model/question/question";
import {PaginatedContent} from "../model/paginatedContent";
import {Answer} from "../model/question/answer";
import {User} from "../model/user/user";
import {Book} from "../model/book/book";

describe('QuestionWithDataService', () => {

  let service: QuestionWithDataService;
  let questionService: QuestionService;
  let userService: UserService;
  let bookService: BookService;
  let authService: AuthService;

  const mockQuestion: Question = { book: 'book-url', answer: 'answer-url', self: 'question-url', writer: 'writer-url', questioner: 'questioner-url'};
  const mockAnswer: Answer = { answer: 'answer'}
  const mockQuestioner: User = { id: 1, firstName: 'Yo',  lastName: 'Cuestiono' };
  const mockWriter: User = { id: 2, firstName: 'Yo',  lastName: 'Escribo' };
  const mockBook: Book = { id: 1, title: 'Test Book', writer: 'writer-url'};
  const mockPaginatedQuestions: PaginatedContent<Question> = {
    data: [mockQuestion],
    pagination: { totalCount: 1, pageCount: 1 }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          QuestionWithDataService,
          QuestionService,
          UserService,
          BookService,
          AuthService,
          provideHttpClient(),
          provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(QuestionWithDataService);
    questionService = TestBed.inject(QuestionService);
    userService = TestBed.inject(UserService);
    bookService = TestBed.inject(BookService);
    authService = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return questions asked by the current user', (done) => {
    spyOn(questionService, 'listQuestions').and.returnValue(of(mockPaginatedQuestions));
    spyOn(questionService, 'getAnswer').and.returnValue(of(mockAnswer));
    spyOn(userService, 'getUser').and.returnValue(of(mockWriter));
    spyOn(bookService, 'getBook').and.returnValue(of(mockBook));
    spyOn(authService, 'getLoggedUser').and.returnValue(of(mockQuestioner));

    service.getAskedQuestions(1).subscribe(result => {
      expect(result.data.length).toBe(1);
      expect(result.data[0].bookInfo).toEqual(mockBook);
      expect(result.data[0].writerInfo).toEqual(mockWriter);
      expect(result.data[0].answerInfo).toEqual(mockAnswer);
      expect(questionService.listQuestions).toHaveBeenCalled();
      expect(questionService.getAnswer).toHaveBeenCalledWith('question-url');
      expect(userService.getUser).toHaveBeenCalledWith('writer-url');
      expect(bookService.getBook).toHaveBeenCalledWith('book-url');
      expect(authService.getLoggedUser).toHaveBeenCalledWith();
      done();
    });
  });

  it('should return questions received by the current user', (done) => {
    spyOn(questionService, 'listQuestions').and.returnValue(of(mockPaginatedQuestions));
    spyOn(questionService, 'getAnswer').and.returnValue(of(mockAnswer));
    spyOn(userService, 'getUser').and.returnValue(of(mockQuestioner));
    spyOn(bookService, 'getBook').and.returnValue(of(mockBook));
    spyOn(authService, 'getLoggedUser').and.returnValue(of(mockWriter));

    service.getReceivedQuestions(1, 10, true).subscribe(result => {
      expect(result.data.length).toBe(1);
      expect(result.data[0].bookInfo).toEqual(mockBook);
      expect(result.data[0].questionerInfo).toEqual(mockQuestioner);
      expect(result.data[0].answerInfo).toEqual(mockAnswer);
      expect(questionService.listQuestions).toHaveBeenCalled();
      expect(questionService.getAnswer).toHaveBeenCalledWith('question-url');
      expect(userService.getUser).toHaveBeenCalledWith('questioner-url');
      expect(bookService.getBook).toHaveBeenCalledWith('book-url');
      expect(authService.getLoggedUser).toHaveBeenCalledWith();
      done();
    });
  });
});
