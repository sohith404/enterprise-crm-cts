import { TestBed } from '@angular/core/testing';

import { MarketingAutomationService } from './marketing-automation.service';

describe('MarketingAutomationService', () => {
    let service: MarketingAutomationService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(MarketingAutomationService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
