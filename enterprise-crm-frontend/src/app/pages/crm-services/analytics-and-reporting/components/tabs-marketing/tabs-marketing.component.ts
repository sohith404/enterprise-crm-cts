import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { SalesService } from '../../../sales-automation/service/sales.service';
import { SalesOpportunity, SalesStage } from '../../../../../models/SalesOpportunity';
import { MarketingAutomationService } from '../../../marketing-automation/service/marketing-automation.service';
import { Campaign } from '../../../../../models/Campaign';

@Component({
    selector: 'app-tabs-marketing',
    standalone: false,
    templateUrl: './tabs-marketing.component.html',
    styleUrl: './tabs-marketing.component.scss'
})
export class TabsMarketingComponent {
    allCampaigns!: SalesOpportunity[];

    constructor(private readonly maretingService: MarketingAutomationService) {}

    totalCampaign!: number;
    ongoingCampaign!: number;
    totalCustomerInteractions!: number;

    statistics: {
        title: string;
        count: string;
        iconBg: string;
        iconClass: string;
        closingLine: string;
        closingBg: string;
        footer: string;
    }[] = [];

    checkIfOngoing(closing: Date, today: Date, starting: Date): boolean {
        return today >= starting && today <= closing;
    }

    ngOnInit(): void {
        this.maretingService.getCampaigns().subscribe({
            next: (campaigns: Campaign[]) => {
                this.totalCampaign = campaigns.length;
                this.ongoingCampaign = campaigns.filter((campaign) => this.checkIfOngoing(new Date(campaign.endDate), new Date(), new Date(campaign.startDate))).length;
                this.totalCustomerInteractions = campaigns.map((campaign) => campaign.customerInteractions!).reduce((a, b) => a! + b!, 0);

                this.statistics = [
                    {
                        title: 'Total Campaigns',
                        count: this.totalCampaign.toString(),
                        iconBg: 'bg-blue-100',
                        iconClass: 'pi pi-users text-blue-500',
                        closingLine: '',
                        closingBg: 'text-green-500',
                        footer: ''
                    },
                    {
                        title: 'Ongoing Campaigns',
                        count: this.ongoingCampaign.toString(),
                        iconBg: 'bg-green-100',
                        iconClass: 'pi pi-indian-rupee text-green-500',
                        closingLine: '',
                        closingBg: 'text-red-500',
                        footer: ''
                    },
                    {
                        title: 'Customer Interactions',
                        count: this.totalCustomerInteractions.toString(),
                        iconBg: 'bg-purple-100',
                        iconClass: 'pi pi-trophy text-purple-500',
                        closingLine: '',
                        closingBg: 'text-green-500',
                        footer: ''
                    }
                ];
            }
        });
    }
}
