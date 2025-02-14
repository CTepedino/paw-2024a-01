import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptPopupComponent } from './accept-popup.component';

describe('AcceptPopupComponent', () => {
  let component: AcceptPopupComponent;
  let fixture: ComponentFixture<AcceptPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcceptPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
