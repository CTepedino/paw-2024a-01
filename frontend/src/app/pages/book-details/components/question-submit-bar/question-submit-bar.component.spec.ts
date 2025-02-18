import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionSubmitBarComponent } from './question-submit-bar.component';

describe('QuestionSubmitBarComponent', () => {
  let component: QuestionSubmitBarComponent;
  let fixture: ComponentFixture<QuestionSubmitBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionSubmitBarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuestionSubmitBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
