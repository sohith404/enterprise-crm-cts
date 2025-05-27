import { Component, OnInit } from '@angular/core';
import { CampaignReachByType, MarketingAutomationService } from '../../service/marketing-automation.service';

@Component({
    selector: 'app-reach-analysis',
    standalone: false,
    templateUrl: './reach-analysis.component.html',
    styleUrl: './reach-analysis.component.scss'
})
export class ReachAnalysisComponent implements OnInit {
    campaignReachByType: CampaignReachByType | null = null;
    errorMessage: string = '';
    maxAverageInteraction: number = 0;

    constructor(private marketingService: MarketingAutomationService) {}

    ngOnInit(): void {
        this.loadCampaignReachAnalysis();
    }

    loadCampaignReachAnalysis(): void {
        const requestPayload: CampaignReachByType = {};

        this.marketingService.reachAnalysis(requestPayload).subscribe(
            (data) => {
                this.campaignReachByType = data;
                this.calculateMaxAverageInteraction();
            },
            (error) => {
                this.errorMessage = 'Failed to load campaign reach analysis.';
                console.error('Error loading campaign reach analysis:', error);
            }
        );
    }

    calculateMaxAverageInteraction(): void {
        if (this.campaignReachByType) {
            this.maxAverageInteraction = Object.values(this.campaignReachByType).reduce((max, typeData) => Math.max(max, typeData?.averageInteractions || 0), 0);
        }
    }

    getCampaignTypes(): string[] {
        return this.campaignReachByType ? Object.keys(this.campaignReachByType) : [];
    }
}
