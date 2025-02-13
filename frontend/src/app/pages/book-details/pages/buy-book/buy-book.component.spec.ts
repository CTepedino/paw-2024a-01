import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyBookComponent } from './buy-book.component';

describe('BuyBookComponent', () => {
  let component: BuyBookComponent;
  let fixture: ComponentFixture<BuyBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuyBookComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuyBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
