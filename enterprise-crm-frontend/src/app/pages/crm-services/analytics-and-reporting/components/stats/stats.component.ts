import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SalesOpportunity, SalesStage } from '../../../../../models/SalesOpportunity';
import { CurrencyPipe } from '@angular/common';
import { CustomerProfile } from '../../../../../models/CustomerProfile';
import { SupportTicket } from '../../../../../models/SupportTicket';
@Component({
    selector: 'app-analytics-stats',
    standalone: false,
    templateUrl: './stats.component.html',
    styleUrl: './stats.component.scss'
})
export class AnalyticsStatsComponent implements OnInit {
    @Input({ required: true }) allSales$!: Observable<SalesOpportunity[]>;
    @Input({ required: true }) allCustomers$!: Observable<CustomerProfile[]>;
    @Input({ required: true }) allSupports$!: Observable<SupportTicket[]>;

    constructor(private currencyPipe: CurrencyPipe) {}

    //for sales

    totalLeads!: number;
    totalRevenue!: number;
    winRate!: number;
    lostRate!: number;
    closingToday!: number;
    revenueLost!: number;

    statistics: {
        title: string;
        count: string;
        iconBg: string;
        iconClass: string;
        closingLine: string;
        closingBg: string;
        footer: string;
    }[] = [];

    //helper to compare dates
    isSameDay(closingDate: Date, today: Date) {
        return closingDate.getFullYear() === today.getFullYear() && closingDate.getMonth() === today.getMonth() && closingDate.getDate() === today.getDate();
    }

    ngOnInit(): void {
        this.allSales$.subscribe((sales) => {
            this.revenueLost = sales
                .filter((sale) => sale.salesStage == SalesStage.CLOSED_LOST)
                .map((sale) => sale.estimatedValue)
                .reduce((a, b) => a + b, 0);
            this.closingToday = sales.filter((sale) => this.isSameDay(new Date(sale.closingDate), new Date())).length;
            this.totalLeads = sales.length;
            this.totalRevenue = sales
                .filter((sale) => sale.salesStage === 'CLOSED_WON')
                .map((sale) => sale.estimatedValue)
                .reduce((a, b) => a + b, 0);
            const won = sales.filter((sale) => sale.salesStage === 'CLOSED_WON').length;
            const lost = sales.filter((sale) => sale.salesStage === 'CLOSED_LOST').length;
            this.winRate = +((won / sales.length) * 100).toFixed(2);
            this.lostRate = +((lost / sales.length) * 100).toFixed(2);
            this.statistics.push(
                {
                    title: 'Total Revenue',
                    count: this.totalRevenue.toFixed(2).toString(),
                    iconBg: 'bg-green-100',
                    iconClass: 'pi pi-indian-rupee text-green-500',
                    closingLine: `${this.currencyPipe.transform(this.revenueLost, 'INR')}`,
                    closingBg: 'text-red-500',
                    footer: 'Revenue lost:'
                },
                {
                    title: 'Avg Deal Size',
                    count: this.winRate.toString() + '%',
                    iconBg: 'bg-purple-100',
                    iconClass: 'pi pi-trophy text-purple-500',
                    closingLine: `${this.lostRate}%`,
                    closingBg: 'text-green-500',
                    footer: 'Lose rate:'
                }
            );
        });

        this.allCustomers$.subscribe((customers) => {
            this.statistics.push({
                title: 'Total Users',
                count: customers.length.toString(),
                iconBg: 'bg-blue-100',
                iconClass: 'pi pi-users text-blue-500',
                closingLine: `${customers.filter((customer) => customer.purchaseHistory.length > 3).length}`,
                closingBg: 'text-green-500',
                footer: 'Active customers:'
            });
        });

        this.allSupports$.subscribe((supportTickets) => {
            let totalTickets = supportTickets.length;
            let closedTickets = supportTickets.filter((ticket) => ticket.status === 'CLOSED').length;
            let resolutionRate = totalTickets > 0 ? (closedTickets / totalTickets) * 100 : 0;

            this.statistics.push({
                title: 'Total Tickets',
                count: totalTickets.toString(),
                iconBg: 'bg-blue-100',
                iconClass: 'pi pi-users text-blue-500',
                closingLine: `${resolutionRate.toFixed(2)}%`,
                closingBg: 'text-green-500',
                footer: 'Resolution Rate:'
            });
        });
    }
}
