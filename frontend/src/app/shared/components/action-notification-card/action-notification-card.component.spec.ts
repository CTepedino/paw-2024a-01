import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionNotificationCardComponent } from './action-notification-card.component';

describe('ActionNotificationCardComponent', () => {
  let component: ActionNotificationCardComponent;
  let fixture: ComponentFixture<ActionNotificationCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActionNotificationCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActionNotificationCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
