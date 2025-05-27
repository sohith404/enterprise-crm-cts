import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesAutomationComponent } from './sales-automation.component';

describe('SalesAutomationComponent', () => {
    let component: SalesAutomationComponent;
    let fixture: ComponentFixture<SalesAutomationComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SalesAutomationComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(SalesAutomationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
