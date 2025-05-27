import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReachAnalysisComponent } from './reach-analysis.component';

describe('ReachAnalysisComponent', () => {
    let component: ReachAnalysisComponent;
    let fixture: ComponentFixture<ReachAnalysisComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ReachAnalysisComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(ReachAnalysisComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
