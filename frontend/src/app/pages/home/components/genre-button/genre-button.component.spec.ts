import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenreButtonComponent } from './genre-button.component';

describe('GenreButtonComponent', () => {
  let component: GenreButtonComponent;
  let fixture: ComponentFixture<GenreButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenreButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenreButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
