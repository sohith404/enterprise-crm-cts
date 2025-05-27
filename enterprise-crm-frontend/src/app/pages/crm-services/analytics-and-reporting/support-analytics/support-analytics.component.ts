import { ChangeDetectorRef, Component } from '@angular/core';
import { Observable } from 'rxjs';
import { SupportReport } from '../../../../models/Analytics';
import { CustomerSupportService } from '../../customer-support/service/customer-support.service';
import { AnalyticsService } from '../service/analytics.service';
import { Store } from '@ngrx/store';
import { Notification } from '../../../../models/Notification';
import { addNotification } from '../../../../store/notifications/notiffications.actions';

@Component({
    selector: 'app-support-analytics',
    standalone: false,
    templateUrl: './support-analytics.component.html',
    styleUrl: './support-analytics.component.scss'
})
export class SupportAnalyticsComponent {
    data: any;
    options: any;
    barData: any;
    barOptions: any;

    reportRes$!: Observable<SupportReport>;
    pieChartData!: { labels: string[]; data: number[] };
    barChartData!: { labels: string[]; data: number[] };

    constructor(
        private readonly cd: ChangeDetectorRef,
        private readonly supportService: CustomerSupportService,
        private readonly analyticsService: AnalyticsService,
        private readonly store: Store
    ) {}

    ngOnInit() {
        this.loadReport();
        this.loadInitialDataForChart();
        this.cd.markForCheck();
        const newNotification: Notification = {
            heading: 'Analytics and Reporting',
            description: 'Generated new customer-support report.',
            time: new Date().toLocaleTimeString()
        };
        this.store.dispatch(addNotification({ notification: newNotification }));
    }

    loadInitialDataForChart() {
        this.reportRes$.subscribe({
            next: (report: SupportReport) => {
                this.pieChartData = {
                    labels: ['CLOSED', 'OPEN'],
                    data: [report.dataPoints.closedTickets, report.dataPoints.openTickets]
                };

                this.barChartData = {
                    labels: Object.keys(report.dataPoints.ticketsPerAgent),
                    data: Object.values(report.dataPoints.ticketsPerAgent)
                };
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
                    label: 'Tickets assigned per agent',
                    data: this.barChartData.data,
                    backgroundColor: ['rgb(107, 114, 128, 0.2)', 'rgba(249, 115, 22, 0.2)', 'rgba(6, 182, 212, 0.2)', 'rgba(139, 92, 246, 0.2)'],
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
        this.reportRes$ = this.analyticsService.generateSupportReport();
        this.loadInitialDataForChart();
    }
}
