import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TutorialCardsComponent } from './tutorial-cards.component';

describe('TutorialCardsComponent', () => {
  let component: TutorialCardsComponent;
  let fixture: ComponentFixture<TutorialCardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TutorialCardsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TutorialCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
