import { isPlatformBrowser } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { MarketingAutomationService } from '../../../marketing-automation/service/marketing-automation.service';
import { Campaign } from '../../../../../models/Campaign';

@Component({
    selector: 'app-marketingmodule',
    standalone: false,
    templateUrl: './marketingmodule.component.html',
    styleUrl: './marketingmodule.component.scss'
})
export class MarketingmoduleComponent implements OnInit {
    data: any;
    options: any;
    platformId = inject(PLATFORM_ID);
    private cd = inject(ChangeDetectorRef);
    latestCampaigns: Campaign[] = [];
    campaignData!: Campaign[];
    constructor(private readonly marketingService: MarketingAutomationService) {}

    ngOnInit() {
        this.marketingService.getCampaigns().subscribe({
            next: (campaigns) => {
                this.campaignData = campaigns;
                this.loadLatestCampaigns(this.campaignData);
                this.initChart();
            }
        });
    }

    loadLatestCampaigns(campaigns: Campaign[]) {
        // Sort campaigns by startDate in descending order (latest first)
        const sortedCampaigns = [...campaigns].sort((a, b) => new Date(b.startDate).getTime() - new Date(a.startDate).getTime());

        const numberOfLatestCampaigns = 5; // Display the top 5 latest campaigns
        this.latestCampaigns = sortedCampaigns.slice(0, numberOfLatestCampaigns);
    }

    initChart() {
        if (isPlatformBrowser(this.platformId) && this.latestCampaigns.length > 0) {
            const documentStyle = getComputedStyle(document.documentElement);
            const textColor = documentStyle.getPropertyValue('--p-text-color');
            const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
            const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

            this.data = {
                labels: this.latestCampaigns.map((campaign) => campaign.name),
                datasets: [
                    {
                        label: 'Customer Interactions',
                        backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
                        borderColor: documentStyle.getPropertyValue('--p-cyan-500'),
                        data: this.latestCampaigns.map((campaign) => campaign.customerInteractions)
                    }
                ]
            };

            this.options = {
                indexAxis: 'y',
                maintainAspectRatio: false,
                aspectRatio: 0.8,
                plugins: {
                    legend: {
                        labels: {
                            color: textColor
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Customer Interactions',
                            color: textColorSecondary,
                            font: {
                                size: 14,
                                weight: 'bold'
                            }
                        },
                        ticks: {
                            color: textColorSecondary,
                            font: {
                                weight: 500
                            }
                        },
                        grid: {
                            color: surfaceBorder,
                            drawBorder: false
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Campaign',
                            color: textColorSecondary,
                            font: {
                                size: 14,
                                weight: 'bold'
                            }
                        },
                        ticks: {
                            color: textColorSecondary
                        },
                        grid: {
                            color: surfaceBorder,
                            drawBorder: false
                        }
                    }
                }
            };
            this.cd.markForCheck();
        } else {
            this.data = {
                labels: [],
                datasets: []
            };
        }
    }
}
