import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthlySalesTrendComponent } from './monthly-sales-trend.component';

describe('MonthlySalesTrendComponent', () => {
    let component: MonthlySalesTrendComponent;
    let fixture: ComponentFixture<MonthlySalesTrendComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MonthlySalesTrendComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(MonthlySalesTrendComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
