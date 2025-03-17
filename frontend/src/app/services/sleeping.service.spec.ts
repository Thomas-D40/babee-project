import { TestBed } from '@angular/core/testing';

import { SleepingService } from './sleeping.service';

describe('SleepingService', () => {
  let service: SleepingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SleepingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
