import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabsMarketingComponent } from './tabs-marketing.component';

describe('TabsMarketingComponent', () => {
    let component: TabsMarketingComponent;
    let fixture: ComponentFixture<TabsMarketingComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [TabsMarketingComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(TabsMarketingComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
