import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabsCustomerComponent } from './tabs-customer.component';

describe('TabsCustomerComponent', () => {
    let component: TabsCustomerComponent;
    let fixture: ComponentFixture<TabsCustomerComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [TabsCustomerComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(TabsCustomerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
