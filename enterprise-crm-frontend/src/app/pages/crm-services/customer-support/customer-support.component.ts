import { Component, OnInit } from '@angular/core';
import { CustomerSupportService } from './service/customer-support.service';
import { Observable } from 'rxjs';
import { SupportTicket } from '../../../models/SupportTicket';

@Component({
    selector: 'app-customer-support',
    standalone: false,
    templateUrl: './customer-support.component.html',
    styleUrls: ['./customer-support.component.scss']
})
export class CustomerSupportComponent implements OnInit {
    constructor(private readonly customerSupportService: CustomerSupportService) {}

    allTickets$!: Observable<SupportTicket[]>;

    ngOnInit(): void {
        this.allTickets$ = this.customerSupportService.getAllTickets();
    }
}
