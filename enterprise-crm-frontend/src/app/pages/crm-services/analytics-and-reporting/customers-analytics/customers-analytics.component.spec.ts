import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomersAnalyticsComponent } from './customers-analytics.component';

describe('CustomersAnalyticsComponent', () => {
    let component: CustomersAnalyticsComponent;
    let fixture: ComponentFixture<CustomersAnalyticsComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CustomersAnalyticsComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(CustomersAnalyticsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
