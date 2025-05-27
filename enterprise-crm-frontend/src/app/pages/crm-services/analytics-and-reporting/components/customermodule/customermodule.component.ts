import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CustomerProfile } from '../../../../../models/CustomerProfile';
import { CustomersService } from '../../../customer-data-management/service/customers.service';

@Component({
    selector: 'app-customermodule',
    standalone: false,
    templateUrl: './customermodule.component.html',
    styleUrl: './customermodule.component.scss'
})
export class CustomermoduleComponent implements OnInit {
    regionData: any[] = [];

    // Your raw data
    customersData!: CustomerProfile[];
    data: any;

    options: any;

    constructor(
        private cd: ChangeDetectorRef,
        private readonly customerService: CustomersService
    ) {}

    ngOnInit() {
        this.customerService.getCustomers().subscribe({
            next: (customers) => {
                this.customersData = customers;
                this.initChart();
            }
        });
    }

    initChart() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--p-text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
        const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');
        const customerByRegion = new Map<string, number>();
        this.customersData.forEach((customer) => {
            const region = customer.segmentationData.Region;
            customerByRegion.set(region, (customerByRegion.get(region) || 0) + 1);
        });
        const regions = [...new Set([...this.customersData.map((customer) => customer.segmentationData.Region)])];
        const finalValues: number[] = [];
        regions.forEach((region) => finalValues.push(customerByRegion.get(region)!));

        this.data = {
            labels: regions,
            datasets: [
                {
                    label: 'Customers by Region',
                    backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
                    borderColor: documentStyle.getPropertyValue('--p-cyan-500'),
                    data: finalValues
                }
            ]
        };

        this.options = {
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
    }
}
