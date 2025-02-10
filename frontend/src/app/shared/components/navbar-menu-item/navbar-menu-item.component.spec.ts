import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarMenuItemComponent } from './navbar-menu-item.component';

describe('NavbarMenuItemComponent', () => {
  let component: NavbarMenuItemComponent;
  let fixture: ComponentFixture<NavbarMenuItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarMenuItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarMenuItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
