import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SalesOpportunity, SalesStage } from '../../../../../models/SalesOpportunity';
import { CurrencyPipe } from '@angular/common';

@Component({
    selector: 'app-stats',
    standalone: false,
    templateUrl: './stats.component.html',
    styleUrl: './stats.component.scss'
})
export class StatsComponent implements OnInit {
    @Input({ required: true }) allSales$!: Observable<SalesOpportunity[]>;

    constructor(private currencyPipe: CurrencyPipe) {}

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
