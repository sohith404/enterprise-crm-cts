import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerManagementHeaderComponent } from './customer-management-header.component';

describe('CustomerManagementHeaderComponent', () => {
    let component: CustomerManagementHeaderComponent;
    let fixture: ComponentFixture<CustomerManagementHeaderComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CustomerManagementHeaderComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(CustomerManagementHeaderComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
