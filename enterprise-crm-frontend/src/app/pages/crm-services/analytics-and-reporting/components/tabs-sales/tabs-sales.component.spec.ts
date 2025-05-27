import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabsSalesComponent } from './tabs-sales.component';

describe('TabsSalesComponent', () => {
    let component: TabsSalesComponent;
    let fixture: ComponentFixture<TabsSalesComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [TabsSalesComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(TabsSalesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
