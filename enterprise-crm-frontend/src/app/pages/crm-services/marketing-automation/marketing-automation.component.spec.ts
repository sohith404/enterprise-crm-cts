import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketingAutomationComponent } from './marketing-automation.component';

describe('MarketingAutomationComponent', () => {
    let component: MarketingAutomationComponent;
    let fixture: ComponentFixture<MarketingAutomationComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MarketingAutomationComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(MarketingAutomationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
