import { Component } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { CustomerProfile } from '../../../../../models/CustomerProfile';
import { CustomersService } from '../../../customer-data-management/service/customers.service';
import { Observable, of } from 'rxjs';

@Component({
    selector: 'app-tabs-customer',
    standalone: false,
    templateUrl: './tabs-customer.component.html',
    styleUrl: './tabs-customer.component.scss'
})
export class TabsCustomerComponent {
    constructor(
        private readonly currencyPipe: CurrencyPipe,
        private readonly customersService: CustomersService
    ) {}

    activeCustomers!: number;
    totalCustomers!: number;
    topRegion!: string;
    trendingInterest!: string;
    customerCountInterest!: number;
    customerCountRegion!: number;

    statistics: {
        title: string;
        count: string;
        iconBg: string;
        iconClass: string;
        closingLine: string;
        closingBg: string;
        footer: string;
    }[] = [];

    ngOnInit(): void {
        this.customersService.getCustomers().subscribe((customers) => {
            this.totalCustomers = customers.length;
            this.activeCustomers = customers.filter((customer) => customer.purchaseHistory.length > 3).length;
            const regionCount: Record<string, number> = customers.reduce((acc: Record<string, number>, customer) => {
                const region = customer.segmentationData.Region;
                acc[region] = (acc[region] ?? 0) + 1;
                return acc;
            }, {});

            const interestCount: Record<string, number> = customers.reduce((acc: Record<string, number>, customer) => {
                const interest = customer.segmentationData.Interest;
                acc[interest] = (acc[interest] ?? 0) + 1;
                return acc;
            }, {});

            this.topRegion = Object.keys(regionCount).reduce((top, region) => {
                if (!top || regionCount[region] > regionCount[top]) {
                    this.customerCountRegion = regionCount[region];
                    return region;
                }
                return top;
            }, '');

            this.trendingInterest = Object.keys(interestCount).reduce((top, interest) => {
                if (!top || interestCount[interest] > interestCount[interest]) {
                    this.customerCountInterest = interestCount[interest];
                    return interest;
                }
                return top;
            }, '');

            this.statistics = [
                {
                    title: 'Active Customers',
                    count: this.activeCustomers.toString(),
                    iconBg: 'bg-blue-100',
                    iconClass: 'pi pi-users text-blue-500',
                    closingLine: `${this.totalCustomers}`,
                    closingBg: 'text-green-500',
                    footer: 'Total customers:'
                },
                {
                    title: 'Top Region',
                    count: this.topRegion,
                    iconBg: 'bg-green-100',
                    iconClass: 'pi pi-indian-rupee text-green-500',
                    closingLine: `${this.customerCountRegion}`,
                    closingBg: 'text-green-500',
                    footer: 'Customer count:'
                },
                {
                    title: 'Trending Interest',
                    count: this.trendingInterest,
                    iconBg: 'bg-purple-100',
                    iconClass: 'pi pi-trophy text-purple-500',
                    closingLine: `${this.customerCountInterest}`,
                    closingBg: 'text-green-500',
                    footer: 'Customer count:'
                }
            ];
        });
    }
}
