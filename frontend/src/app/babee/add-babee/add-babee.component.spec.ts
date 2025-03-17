import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBabeeComponent } from './add-babee.component';

describe('AddBabeeComponent', () => {
  let component: AddBabeeComponent;
  let fixture: ComponentFixture<AddBabeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddBabeeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddBabeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
