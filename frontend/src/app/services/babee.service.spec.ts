import { TestBed } from '@angular/core/testing';

import { BabeeService } from './babee.service';

describe('BabeeService', () => {
  let service: BabeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BabeeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
