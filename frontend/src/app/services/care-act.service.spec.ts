import { TestBed } from '@angular/core/testing';

import { CareActService } from './care-act.service';

describe('CareActService', () => {
  let service: CareActService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CareActService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
