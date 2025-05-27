import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { SalesService } from '../../../sales-automation/service/sales.service';
import { SalesOpportunity, SalesStage } from '../../../../../models/SalesOpportunity';

@Component({
    selector: 'app-tabs-sales',
    templateUrl: './tabs-sales.component.html',
    styleUrls: ['./tabs-sales.component.scss'],
    standalone: false
})
export class TabsSalesComponent implements OnInit {
    allSales!: SalesOpportunity[];

    constructor(
        private readonly currencyPipe: CurrencyPipe,
        private readonly salesService: SalesService
    ) {}

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

    isSameDay(closingDate: Date, today: Date) {
        return closingDate.getFullYear() === today.getFullYear() && closingDate.getMonth() === today.getMonth() && closingDate.getDate() === today.getDate();
    }

    ngOnInit(): void {
        this.salesService.salesList$.subscribe((sales) => {
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
            this.statistics = [
                {
                    title: 'Total Leads',
                    count: this.totalLeads.toString(),
                    iconBg: 'bg-blue-100',
                    iconClass: 'pi pi-users text-blue-500',
                    closingLine: `${this.closingToday}`,
                    closingBg: 'text-green-500',
                    footer: 'Leads closing today:'
                },
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
                    title: 'Win Rate',
                    count: this.winRate.toString() + '%',
                    iconBg: 'bg-purple-100',
                    iconClass: 'pi pi-trophy text-purple-500',
                    closingLine: `${this.lostRate}%`,
                    closingBg: 'text-green-500',
                    footer: 'Lose rate:'
                }
            ];
        });
    }
}
