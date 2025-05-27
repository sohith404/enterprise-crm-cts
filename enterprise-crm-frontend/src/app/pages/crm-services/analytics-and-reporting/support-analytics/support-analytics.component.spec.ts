import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportAnalyticsComponent } from './support-analytics.component';

describe('SupportAnalyticsComponent', () => {
    let component: SupportAnalyticsComponent;
    let fixture: ComponentFixture<SupportAnalyticsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SupportAnalyticsComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(SupportAnalyticsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
