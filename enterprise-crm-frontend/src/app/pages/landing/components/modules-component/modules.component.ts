import { Component, OnInit } from '@angular/core';
import { SalesService } from '../../../crm-services/sales-automation/service/sales.service';
import { CustomersService } from '../../../crm-services/customer-data-management/service/customers.service';
import { CustomerSupportService } from '../../../crm-services/customer-support/service/customer-support.service';
import { MarketingAutomationService } from '../../../crm-services/marketing-automation/service/marketing-automation.service';
import { AnalyticsService } from '../../../crm-services/analytics-and-reporting/service/analytics.service';

@Component({
    selector: 'app-modules',
    templateUrl: './modules.component.html',
    styleUrls: ['./modules.component.scss'],
    standalone: false
})
export class ModulesComponent implements OnInit {
    totalCustomers!: number;
    estimatedValue!: number;
    activeCampaigns!: number;
    openTickets!: number;
    lastUpdated!: string;
    modules: {
        title: string;
        description: string;
        icon: string;
        href: string;
        stats: string;
        color: string;
    }[] = [];

    constructor(
        private readonly salesService: SalesService,
        private readonly customerService: CustomersService,
        private readonly supportService: CustomerSupportService,
        private readonly marketingService: MarketingAutomationService,
        private readonly analyticsService: AnalyticsService
    ) {}

    isOngoing(startDate: string, endDate: string): boolean {
        const today = new Date();
        const start = new Date(startDate);
        const end = new Date(endDate);
        return today >= start && today <= end;
    }

    ngOnInit(): void {
        this.salesService.getAllSales().subscribe((sales) => {
            this.estimatedValue = sales.map((sale) => sale.estimatedValue).reduce((a, b) => a + b, 0);
            this.modules.push({
                title: 'Sales',
                description: 'Track deals and revenue pipeline',
                icon: 'indian-rupee',
                href: '/pages/services/sales-automation',
                stats: `₹${this.estimatedValue.toFixed(2)} pipeline`,
                color: 'bg-green-100 text-green-700'
            });
        });
        this.customerService.getCustomers().subscribe((customers) => {
            this.totalCustomers = customers.length;
            this.modules.push({
                title: 'Customers',
                description: 'Manage customer profiles and interactions',
                icon: 'users',
                href: '/pages/services/customer-data-management',
                stats: `${this.totalCustomers} total`,
                color: 'bg-blue-100 text-blue-700'
            });
        });
        this.supportService.getAllTickets().subscribe((tickets) => {
            this.openTickets = tickets.map((ticket) => ticket.status === 'OPEN').length;
            this.modules.push({
                title: 'Support',
                description: 'Handle customer support tickets',
                icon: 'phone',
                href: '/pages/services/customer-support',
                stats: `${this.openTickets} open tickets`,
                color: 'bg-amber-100 text-amber-700'
            });
        });
        this.analyticsService.getAllReports().subscribe((reports) => {
            const lastReportDate = new Date(reports.reverse().at(0)?.generatedDate!);
            const now = new Date();
            const diffInMs = now.getTime() - lastReportDate.getTime();
            const diffInHours = Math.floor(diffInMs / (1000 * 60 * 60));
            const diffInDays = Math.floor(diffInHours / 24);

            if (diffInDays > 0) {
                this.lastUpdated = `${diffInDays} day(s) ago`;
            } else {
                this.lastUpdated = `${diffInHours} hour(s) ago`;
            }

            this.modules.push({
                title: 'Analytics',
                description: 'View business performance metrics',
                icon: 'chart-bar',
                href: '/pages/services/analytics-and-reporting',
                stats: `${this.lastUpdated}`,
                color: 'bg-indigo-100 text-indigo-700'
            });
        });
        this.marketingService.getCampaigns().subscribe((campaigns) => {
            this.activeCampaigns = campaigns.filter((campaign) => new Date() >= campaign.startDate && new Date() <= campaign.endDate).length;
            this.modules.push({
                title: 'Marketing',
                description: 'Create and monitor campaigns',
                icon: 'megaphone',
                href: '/pages/services/marketing-automation',
                stats: `${this.activeCampaigns} active campaigns`,
                color: 'bg-purple-100 text-purple-700'
            });
        });
    }
}
