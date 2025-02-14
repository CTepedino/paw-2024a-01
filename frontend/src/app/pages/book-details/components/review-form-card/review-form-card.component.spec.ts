
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewFormCardComponent } from './review-form-card.component';

describe('ReviewFormCardComponent', () => {
  let component: ReviewFormCardComponent;
  let fixture: ComponentFixture<ReviewFormCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewFormCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewFormCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
