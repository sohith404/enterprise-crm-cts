import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportmoduleComponent } from './supportmodule.component';

describe('SupportmoduleComponent', () => {
    let component: SupportmoduleComponent;
    let fixture: ComponentFixture<SupportmoduleComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SupportmoduleComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(SupportmoduleComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
