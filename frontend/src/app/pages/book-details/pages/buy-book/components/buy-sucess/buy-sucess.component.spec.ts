import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuySucessComponent } from './buy-sucess.component';

describe('BuySucessComponent', () => {
  let component: BuySucessComponent;
  let fixture: ComponentFixture<BuySucessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuySucessComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuySucessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
