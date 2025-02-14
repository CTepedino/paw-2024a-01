import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecievedQuestionsComponent } from './recieved-questions.component';

describe('RecievedQuestionsComponent', () => {
  let component: RecievedQuestionsComponent;
  let fixture: ComponentFixture<RecievedQuestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecievedQuestionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecievedQuestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
