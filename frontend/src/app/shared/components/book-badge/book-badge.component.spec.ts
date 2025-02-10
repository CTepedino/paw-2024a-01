import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookBadgeComponent } from './book-badge.component';

describe('BookBadgeComponent', () => {
  let component: BookBadgeComponent;
  let fixture: ComponentFixture<BookBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookBadgeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
