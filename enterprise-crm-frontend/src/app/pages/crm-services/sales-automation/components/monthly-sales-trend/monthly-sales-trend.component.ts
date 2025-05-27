import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { debounceTime, Observable, Subscription } from 'rxjs';
import { LayoutService } from '../../../../../layout/service/layout.service';
import { SalesOpportunity, SalesStage } from '../../../../../models/SalesOpportunity';

interface MonthlySales {
    monthYear: string;
    value: number;
}

@Component({
    selector: 'app-monthly-sales-trend',
    standalone: false,
    templateUrl: './monthly-sales-trend.component.html',
    styleUrl: './monthly-sales-trend.component.scss'
})
export class MonthlySalesTrendComponent implements OnInit, OnDestroy {
    lineData: any;
    lineOptions: any;
    subscription: Subscription;
    wonDataPoints: MonthlySales[] = []; // Initialize wonDataPoints
    lostDataPoints: MonthlySales[] = []; // Initialize lostDataPoints
    totalDataPoints: MonthlySales[] = []; // Initialize totalDataPoints

    @Input({ required: true }) allSales$!: Observable<SalesOpportunity[]>;

    getPast6MonthsSalesTrendArray(data: SalesOpportunity[], salesStage: string, forAll: boolean): MonthlySales[] {
        const monthlySalesMap: { [monthYear: string]: number } = {};
        const currentDate = new Date();
        const currentYearFull = currentDate.getFullYear();
        const currentYearShort = currentYearFull % 100;
        const currentMonthIndex = currentDate.getMonth(); // 0-indexed
        const monthNamesShort = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];
        const result: MonthlySales[] = [];

        const past6Months: string[] = [];
        for (let i = 5; i >= 0; i--) {
            let yearShort = currentYearShort;
            let monthIndex = currentMonthIndex - i;
            if (monthIndex < 0) {
                yearShort--;
                monthIndex += 12;
            }
            const monthName = monthNamesShort[monthIndex];
            const yearString = yearShort.toString().padStart(2, '0');
            past6Months.push(`${monthName}'${yearString}`);
            monthlySalesMap[`${monthName}'${yearString}`] = 0;
        }

        for (const sale of data) {
            if (forAll) {
                const closingDate = new Date(sale.closingDate);
                const year = closingDate.getFullYear();
                const month = closingDate.getMonth();
                const targetYearShort = year % 100;
                const targetMonthShort = monthNamesShort[month];
                const targetMonthYear = `${targetMonthShort}'${targetYearShort.toString().padStart(2, '0')}`;
                if (monthlySalesMap.hasOwnProperty(targetMonthYear)) {
                    monthlySalesMap[targetMonthYear] += sale.estimatedValue;
                }
            }
            if (sale.salesStage === salesStage) {
                const closingDate = new Date(sale.closingDate);
                const year = closingDate.getFullYear();
                const month = closingDate.getMonth();
                const targetYearShort = year % 100;
                const targetMonthShort = monthNamesShort[month];
                const targetMonthYear = `${targetMonthShort}'${targetYearShort.toString().padStart(2, '0')}`;
                if (monthlySalesMap.hasOwnProperty(targetMonthYear)) {
                    monthlySalesMap[targetMonthYear] += sale.estimatedValue;
                }
            }
        }

        for (const monthYear in monthlySalesMap) {
            if (monthlySalesMap.hasOwnProperty(monthYear)) {
                result.push({ monthYear: monthYear, value: monthlySalesMap[monthYear] });
            }
        }
        return result;
    }

    constructor(private readonly layoutService: LayoutService) {
        this.subscription = this.layoutService.configUpdate$.pipe(debounceTime(25)).subscribe(() => {
            if (this.wonDataPoints && this.wonDataPoints.length > 0) {
                // Check if wonDataPoints has been loaded
                this.initCharts();
            }
        });
    }

    ngOnInit() {
        this.allSales$.subscribe((sales) => {
            this.wonDataPoints = this.getPast6MonthsSalesTrendArray(sales, SalesStage.CLOSED_WON, false);
            this.lostDataPoints = this.getPast6MonthsSalesTrendArray(sales, SalesStage.CLOSED_LOST, false);
            this.totalDataPoints = this.getPast6MonthsSalesTrendArray(sales, '', true);
            this.initCharts(); // Call initCharts when sales data is available
        });
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    initCharts() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
        const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

        this.lineData = {
            labels: this.wonDataPoints.map((dp) => dp.monthYear),
            datasets: [
                {
                    label: 'Revenue Generated',
                    data: this.wonDataPoints.map((dp) => dp.value),
                    fill: false,
                    // backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                    backgroundColor: 'green',
                    borderColor: 'green',
                    tension: 0
                },
                {
                    label: 'Revenue Lost',
                    data: this.lostDataPoints.map((dp) => dp.value),
                    fill: false,
                    // backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                    backgroundColor: 'red',
                    borderColor: 'red',
                    tension: 0
                },
                {
                    label: 'Estimated Revenue',
                    data: this.totalDataPoints.map((dp) => dp.value),
                    fill: false,
                    // backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                    backgroundColor: 'slate',
                    borderColor: 'slate',
                    tension: 0
                }
            ]
        };

        this.lineOptions = {
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
                        color: textColorSecondary
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
    }
}
