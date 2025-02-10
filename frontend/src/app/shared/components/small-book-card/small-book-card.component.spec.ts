import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmallBookCardComponent } from './small-book-card.component';

describe('HomeBookCardComponent', () => {
  let component: SmallBookCardComponent;
  let fixture: ComponentFixture<SmallBookCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SmallBookCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SmallBookCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
