import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesOrderCardComponent } from './sales-order-card.component';

describe('SalesOrderCardComponent', () => {
  let component: SalesOrderCardComponent;
  let fixture: ComponentFixture<SalesOrderCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalesOrderCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SalesOrderCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
