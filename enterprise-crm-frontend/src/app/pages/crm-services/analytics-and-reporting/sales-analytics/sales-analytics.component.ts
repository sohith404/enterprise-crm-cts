import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalesReport } from '../../../../models/Analytics';
import { Observable } from 'rxjs';
import { SalesService } from '../../sales-automation/service/sales.service';
import { AnalyticsService } from '../service/analytics.service';
import { Store } from '@ngrx/store';
import { addNotification } from '../../../../store/notifications/notiffications.actions';
import { Notification } from '../../../../models/Notification';

@Component({
    selector: 'app-sales-analytics',
    standalone: false,
    templateUrl: './sales-analytics.component.html',
    styleUrl: './sales-analytics.component.scss'
})
export class SalesAnalyticsComponent implements OnInit {
    data: any;
    options: any;
    barData: any;
    barOptions: any;

    reportRes$!: Observable<SalesReport>;
    pieChartData!: { labels: string[]; data: number[] };
    barChartData!: { labels: string[]; data: number[] };

    constructor(
        private readonly cd: ChangeDetectorRef,
        private readonly salesService: SalesService,
        private readonly analyticsService: AnalyticsService,
        private readonly store: Store
    ) {}

    ngOnInit() {
        this.loadReport();
        this.loadInitialDataForChart();
        this.cd.markForCheck();
        const newNotification: Notification = {
            heading: 'Analytics and Reporting',
            description: 'Generated new sales report.',
            time: new Date().toLocaleTimeString()
        };
        this.store.dispatch(addNotification({ notification: newNotification }));
    }

    loadInitialDataForChart() {
        this.salesService.salesList$.subscribe({
            next: (sales) => {
                const salesStages = [...new Set(sales.map((sale) => sale.salesStage))];
                const estimatedValue = salesStages.reduce(
                    (map, stage) => {
                        map[stage] = sales.filter((sale) => sale.salesStage === stage).reduce((sum, sale) => sum + sale.estimatedValue, 0);
                        return map;
                    },
                    {} as Record<string, number>
                );
                this.barChartData = { labels: salesStages, data: salesStages.map((stage) => estimatedValue[stage]) };
            },
            complete: () => {
                this.initChart();
            }
        });

        this.reportRes$.subscribe({
            next: (report: SalesReport) => {
                this.pieChartData = {
                    labels: ['In progress', 'Won', 'Lost'],
                    data: [report.dataPoints.opportunitiesInProgress, report.dataPoints.opportunitiesWon, report.dataPoints.opportunitiesLost]
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
                    label: 'Estimated Value in Rs.',
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
        this.reportRes$ = this.analyticsService.generateSalesReport();
        this.loadInitialDataForChart();
    }
}
