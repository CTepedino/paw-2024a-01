import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchasesOrderCardComponent } from './purchases-order-card.component';

describe('PurchasesOrderCardComponent', () => {
  let component: PurchasesOrderCardComponent;
  let fixture: ComponentFixture<PurchasesOrderCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PurchasesOrderCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PurchasesOrderCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
