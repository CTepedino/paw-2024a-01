import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetPasswordSucessComponent } from './reset-password-sucess.component';

describe('ResetPasswordSucessComponent', () => {
  let component: ResetPasswordSucessComponent;
  let fixture: ComponentFixture<ResetPasswordSucessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetPasswordSucessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResetPasswordSucessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
