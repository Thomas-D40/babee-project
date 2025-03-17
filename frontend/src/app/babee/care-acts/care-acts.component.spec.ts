import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CareActsComponent } from './care-acts.component';

describe('CareActsComponent', () => {
  let component: CareActsComponent;
  let fixture: ComponentFixture<CareActsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CareActsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CareActsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
