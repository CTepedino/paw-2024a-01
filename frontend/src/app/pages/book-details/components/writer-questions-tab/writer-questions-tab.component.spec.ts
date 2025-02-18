import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WriterQuestionsTabComponent } from './writer-questions-tab.component';

describe('WriterQuestionsTabComponent', () => {
  let component: WriterQuestionsTabComponent;
  let fixture: ComponentFixture<WriterQuestionsTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WriterQuestionsTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WriterQuestionsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
