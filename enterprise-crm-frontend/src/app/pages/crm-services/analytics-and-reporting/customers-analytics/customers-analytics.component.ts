import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CustomersService } from '../../customer-data-management/service/customers.service';
import { AnalyticsService } from '../service/analytics.service';
import { Observable } from 'rxjs';
import { CustomerReport } from '../../../../models/Analytics';
import { Store } from '@ngrx/store';
import { addNotification } from '../../../../store/notifications/notiffications.actions';
import { Notification } from '../../../../models/Notification';

@Component({
    selector: 'app-customers-analytics',
    standalone: false,
    templateUrl: './customers-analytics.component.html',
    styleUrl: './customers-analytics.component.scss'
})
export class CustomersAnalyticsComponent implements OnInit {
    data: any;
    options: any;
    customerData!: Map<string, number>;
    reportRes$!: Observable<CustomerReport>;

    constructor(
        private readonly cd: ChangeDetectorRef,
        private readonly customerService: CustomersService,
        private readonly analyticsService: AnalyticsService,
        private readonly store: Store
    ) {}

    ngOnInit() {
        this.loadInitialCustomerDataForChart();
        this.loadReport();
        this.cd.markForCheck();
        const newNotification: Notification = {
            heading: 'Analytics and Reporting',
            description: 'Generated new customers report.',
            time: new Date().toLocaleTimeString()
        };
        this.store.dispatch(addNotification({ notification: newNotification }));
    }

    loadInitialCustomerDataForChart() {
        this.customerService.getCustomers().subscribe({
            next: (customers) => {
                this.customerData = new Map<string, number>();
                customers.forEach((customer) => {
                    const region = customer.segmentationData.Region;
                    if (this.customerData.has(region)) {
                        this.customerData.set(region, this.customerData.get(region)! + 1);
                    } else {
                        this.customerData.set(region, 1);
                    }
                });
            },
            complete: () => {
                this.initChart();
            }
        });
    }

    initChart() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');

        this.data = {
            labels: [...this.customerData.keys()],
            datasets: [
                {
                    data: [...this.customerData.values()],
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
        this.cd.markForCheck();
    }

    loadReport() {
        this.reportRes$ = this.analyticsService.generateCustomerReport();
        this.loadInitialCustomerDataForChart();
    }
}
