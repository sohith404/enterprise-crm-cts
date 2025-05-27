import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerDataManagementComponent } from './customer-data-management.component';

describe('CustomerDataManagementComponent', () => {
    let component: CustomerDataManagementComponent;
    let fixture: ComponentFixture<CustomerDataManagementComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CustomerDataManagementComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(CustomerDataManagementComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
