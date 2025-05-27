import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCampaignByIdComponent } from './get-campaign-by-id.component';

describe('GetCampaignByIdComponent', () => {
    let component: GetCampaignByIdComponent;
    let fixture: ComponentFixture<GetCampaignByIdComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [GetCampaignByIdComponent]
        }).compileComponents();

        fixture = TestBed.createComponent(GetCampaignByIdComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
