import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoughtBooksTabComponent } from './bought-books-tab.component';

describe('BoughtBooksTabComponent', () => {
  let component: BoughtBooksTabComponent;
  let fixture: ComponentFixture<BoughtBooksTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoughtBooksTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoughtBooksTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
