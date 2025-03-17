import { TestBed } from '@angular/core/testing';

import { HeathActService } from './heath-act.service';

describe('HeathActService', () => {
  let service: HeathActService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeathActService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
