import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyticsAndReportingComponent } from './analytics-and-reporting.component';

describe('AnalyticsAndReportingComponent', () => {
    let component: AnalyticsAndReportingComponent;
    let fixture: ComponentFixture<AnalyticsAndReportingComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [AnalyticsAndReportingComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(AnalyticsAndReportingComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
