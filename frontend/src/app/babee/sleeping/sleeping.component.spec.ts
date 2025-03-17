import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SleepingComponent } from './sleeping.component';

describe('SleepingComponent', () => {
  let component: SleepingComponent;
  let fixture: ComponentFixture<SleepingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SleepingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SleepingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
