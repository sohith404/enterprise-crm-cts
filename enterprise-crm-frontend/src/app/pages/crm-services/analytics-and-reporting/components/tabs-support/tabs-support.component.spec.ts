import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabsSupportComponent } from './tabs-support.component';

describe('TabsSupportComponent', () => {
    let component: TabsSupportComponent;
    let fixture: ComponentFixture<TabsSupportComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [TabsSupportComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(TabsSupportComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
