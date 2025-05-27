import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MarketingReport } from '../../../../models/Analytics';
import { Observable } from 'rxjs';
import { MarketingAutomationService } from '../../marketing-automation/service/marketing-automation.service';
import { AnalyticsService } from '../service/analytics.service';
import { Campaign } from '../../../../models/Campaign';
import { TreeNode } from 'primeng/api';
import { Store } from '@ngrx/store';
import { addNotification } from '../../../../store/notifications/notiffications.actions';
import { Notification } from '../../../../models/Notification';

@Component({
    selector: 'app-marketing-analytics',
    standalone: false,
    templateUrl: './marketing-analytics.component.html',
    styleUrl: './marketing-analytics.component.scss'
})
export class MarketingAnalyticsComponent implements OnInit {
    data: any;
    options: any;
    barData: any;

    barOptions: any;

    reportRes$!: Observable<MarketingReport>;
    pieChartData!: { labels: string[]; data: number[] };
    barChartData!: { labels: string[]; data: number[] };

    constructor(
        private readonly cd: ChangeDetectorRef,
        private readonly marketingService: MarketingAutomationService,
        private readonly analyticsService: AnalyticsService,
        private readonly store: Store
    ) {}

    tree!: TreeNode[];

    ngOnInit() {
        this.loadReport();
        this.loadInitialDataForChart();
        this.cd.markForCheck();
        const newNotification: Notification = {
            heading: 'Analytics and Reporting',
            description: 'Generated new marketing report.',
            time: new Date().toLocaleTimeString()
        };
        this.store.dispatch(addNotification({ notification: newNotification }));
    }

    loadInitialDataForChart() {
        this.marketingService.getCampaigns().subscribe({
            next: (campaigns) => {
                this.barChartData = { labels: campaigns.map((campaign) => campaign.name), data: campaigns.map((campaign) => campaign.customerInteractions!) };
            },
            complete: () => {
                this.initChart();
            }
        });

        this.reportRes$.subscribe({
            next: (report: MarketingReport) => {
                this.pieChartData = {
                    labels: ['Completed Campaigns', 'Active Campaigns', 'Zero Interaction Campaigns'],
                    data: [report.dataPoints.completedCampaigns, report.dataPoints.activeCampaigns, report.dataPoints.zeroInteractionCampaigns]
                };
                this.tree = [
                    {
                        label: 'Campaigns',
                        expanded: true,
                        styleClass: '!bg-indigo-100 !text-indigo-900 rounded-xl',
                        children: [
                            {
                                label: 'Total Campaigns',
                                expanded: true,
                                styleClass: '!bg-pink-100 !text-pink-900 rounded-xl',
                                children: [
                                    {
                                        label: report.dataPoints.totalCampaigns.toString(),
                                        styleClass: '!bg-pink-100 !text-pink-900 rounded-xl'
                                    }
                                ]
                            },
                            {
                                label: 'Top Campaign',
                                expanded: true,
                                styleClass: '!bg-green-100 !text-green-900 rounded-xl',
                                children: [
                                    {
                                        label: report.dataPoints.topCampaign,
                                        styleClass: '!bg-green-100 !text-green-900 rounded-xl'
                                    }
                                ]
                            },
                            {
                                label: 'Customer Interactions',
                                expanded: true,
                                styleClass: '!bg-orange-100 !text-orange-900 rounded-xl',
                                children: [
                                    {
                                        label: report.dataPoints.totalCustomerInteractions.toString(),
                                        styleClass: '!bg-orange-100 !text-orange-900 rounded-xl'
                                    }
                                ]
                            }
                        ]
                    }
                ];
            },
            complete: () => this.initChart()
        });
    }

    initChart() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
        const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

        this.data = {
            labels: this.pieChartData.labels,
            datasets: [
                {
                    data: this.pieChartData.data,
                    backgroundColor: [documentStyle.getPropertyValue('--p-cyan-500'), documentStyle.getPropertyValue('--p-orange-500'), documentStyle.getPropertyValue('--p-gray-500')],
                    hoverBackgroundColor: [documentStyle.getPropertyValue('--p-cyan-400'), documentStyle.getPropertyValue('--p-orange-400'), documentStyle.getPropertyValue('--p-gray-400')]
                }
            ]
        };

        this.options = {
            plugins: {
                legend: {
                    labels: {
                        usePointStyle: true,
                        color: textColor
                    }
                }
            }
        };

        this.barData = {
            labels: this.barChartData.labels,
            datasets: [
                {
                    label: 'Customer Interactions',
                    data: this.barChartData.data,
                    backgroundColor: ['rgba(249, 115, 22, 0.2)', 'rgba(6, 182, 212, 0.2)', 'rgb(107, 114, 128, 0.2)', 'rgba(139, 92, 246, 0.2)'],
                    borderColor: ['rgb(249, 115, 22)', 'rgb(6, 182, 212)', 'rgb(107, 114, 128)', 'rgb(139, 92, 246)'],
                    borderWidth: 1
                }
            ]
        };

        this.barOptions = {
            plugins: {
                legend: {
                    labels: {
                        color: textColor
                    }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: textColorSecondary
                    },
                    grid: {
                        color: surfaceBorder
                    }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: textColorSecondary
                    },
                    grid: {
                        color: surfaceBorder
                    }
                }
            }
        };
        this.cd.markForCheck();
    }

    loadReport() {
        this.reportRes$ = this.analyticsService.generateMarketingReport();
        this.loadInitialDataForChart();
    }
}
