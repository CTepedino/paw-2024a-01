import { TestBed } from '@angular/core/testing';

import { GenreService } from './genre.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {AuthService} from "./auth.service";
import {provideHttpClient} from "@angular/common/http";

describe('GenreService', () => {
  let service: GenreService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(GenreService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
