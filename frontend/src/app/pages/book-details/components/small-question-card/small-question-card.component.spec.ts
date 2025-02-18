import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmallQuestionCardComponent } from './small-question-card.component';

describe('SmallQuestionCardComponent', () => {
  let component: SmallQuestionCardComponent;
  let fixture: ComponentFixture<SmallQuestionCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SmallQuestionCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SmallQuestionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
