import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotPasswordMailSucessComponent } from './forgot-password-mail-sucess.component';

describe('ForgotPasswordMailSucessComponent', () => {
  let component: ForgotPasswordMailSucessComponent;
  let fixture: ComponentFixture<ForgotPasswordMailSucessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgotPasswordMailSucessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordMailSucessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
