import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookCardSerachComponent } from './book-card-serach.component';

describe('BookCardSearchComponent', () => {
  let component: BookCardSerachComponent;
  let fixture: ComponentFixture<BookCardSerachComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookCardSerachComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookCardSerachComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
