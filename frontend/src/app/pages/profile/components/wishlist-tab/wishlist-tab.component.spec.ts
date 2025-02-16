import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistTabComponent } from './wishlist-tab.component';

describe('WishlistTabComponent', () => {
  let component: WishlistTabComponent;
  let fixture: ComponentFixture<WishlistTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WishlistTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WishlistTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
