import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HealthActsComponent } from './health-acts.component';

describe('HealthActsComponent', () => {
  let component: HealthActsComponent;
  let fixture: ComponentFixture<HealthActsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HealthActsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(HealthActsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
