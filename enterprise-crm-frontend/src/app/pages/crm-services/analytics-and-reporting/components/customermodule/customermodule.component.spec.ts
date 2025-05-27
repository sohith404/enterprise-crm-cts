import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomermoduleComponent } from './customermodule.component';

describe('CustomermoduleComponent', () => {
    let component: CustomermoduleComponent;
    let fixture: ComponentFixture<CustomermoduleComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CustomermoduleComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(CustomermoduleComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
