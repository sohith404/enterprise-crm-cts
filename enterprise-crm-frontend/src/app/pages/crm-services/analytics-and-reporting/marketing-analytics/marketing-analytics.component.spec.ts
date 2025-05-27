import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketingAnalyticsComponent } from './marketing-analytics.component';

describe('MarketingAnalyticsComponent', () => {
    let component: MarketingAnalyticsComponent;
    let fixture: ComponentFixture<MarketingAnalyticsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MarketingAnalyticsComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(MarketingAnalyticsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
