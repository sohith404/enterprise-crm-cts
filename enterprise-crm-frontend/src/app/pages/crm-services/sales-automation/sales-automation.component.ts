import { Component, OnInit } from '@angular/core';
import { SalesService } from './service/sales.service';
import { Observable } from 'rxjs';
import { SalesOpportunity } from '../../../models/SalesOpportunity';

@Component({
    selector: 'app-sales-automation',
    standalone: false,
    templateUrl: './sales-automation.component.html',
    styleUrl: './sales-automation.component.scss'
})
export class SalesAutomationComponent implements OnInit {
    salesList$!: Observable<SalesOpportunity[]>;

    constructor(private readonly salesService: SalesService) {}

    ngOnInit(): void {
        this.salesList$ = this.salesService.salesList$;
        this.salesList$.subscribe((sales) => {});
    }

    title = 'Sales-Automation';
}
