import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';

@Component({
    selector: 'app-header',
    standalone: false,
    templateUrl: './header.component.html',
    styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
    constructor(private readonly router: Router) {}

    @Output() refreshClicked = new EventEmitter<void>();
    @Output() downloadClicked = new EventEmitter<void>();
    @Output() dateRangeChanged = new EventEmitter<string>();

    items!: MenuItem[];

    onRefresh(): void {
        this.refreshClicked.emit();
    }

    onDownload(): void {
        this.downloadClicked.emit();
    }

    onDateRangeChange(event: Event): void {
        const selectedValue = (event.target as HTMLSelectElement).value;
        this.dateRangeChanged.emit(selectedValue);
    }

    ngOnInit() {
        this.items = [
            {
                icon: 'pi pi-users',
                label: 'Generate Customer Report',
                command: () => {
                    this.router.navigate(['pages/services/analytics-and-reporting/customer-reports']);
                }
            },
            {
                icon: 'pi pi-indian-rupee',
                label: 'Generate Sales Report',
                command: () => {
                    this.router.navigate(['pages/services/analytics-and-reporting/sales-reports']);
                }
            },
            {
                icon: 'pi pi-question-circle',
                label: 'Generate Customer Support Report',
                command: () => {
                    this.router.navigate(['pages/services/analytics-and-reporting/support-reports']);
                }
            },
            {
                icon: 'pi pi-briefcase',
                label: 'Generate Marketing Report',
                command: () => {
                    this.router.navigate(['pages/services/analytics-and-reporting/marketing-reports']);
                }
            }
        ];
    }
}
