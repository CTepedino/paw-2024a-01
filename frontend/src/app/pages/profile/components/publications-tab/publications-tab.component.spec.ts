import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicationsTabComponent } from './publications-tab.component';

describe('PublicationsTabComponent', () => {
  let component: PublicationsTabComponent;
  let fixture: ComponentFixture<PublicationsTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicationsTabComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicationsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
