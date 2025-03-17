import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BabeeListComponent } from './babee-list.component';

describe('BabeeListComponent', () => {
  let component: BabeeListComponent;
  let fixture: ComponentFixture<BabeeListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BabeeListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BabeeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
