import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReuploadPopupComponent } from './reupload-popup.component';

describe('ReuploadPopupComponent', () => {
  let component: ReuploadPopupComponent;
  let fixture: ComponentFixture<ReuploadPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReuploadPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReuploadPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
