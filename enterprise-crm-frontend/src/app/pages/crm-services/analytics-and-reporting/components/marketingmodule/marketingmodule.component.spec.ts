import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketingmoduleComponent } from './marketingmodule.component';

describe('MarketingmoduleComponent', () => {
    let component: MarketingmoduleComponent;
    let fixture: ComponentFixture<MarketingmoduleComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MarketingmoduleComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(MarketingmoduleComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
