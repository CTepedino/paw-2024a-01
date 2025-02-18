import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionsTabComponent } from './questions-tab.component';

describe('QuestionsTabComponent', () => {
  let component: QuestionsTabComponent;
  let fixture: ComponentFixture<QuestionsTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionsTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuestionsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
