import { SupportTicket } from './../../../models/SupportTicket';
import { CustomerProfile } from './../../../models/CustomerProfile';
import { Component, OnInit } from '@angular/core';
import { SalesService } from '../sales-automation/service/sales.service';
import { SalesOpportunity } from '../../../models/SalesOpportunity';
import { Observable, of } from 'rxjs';
import { CustomersService } from '../customer-data-management/service/customers.service';
import { CustomerSupportService } from '../customer-support/service/customer-support.service';
import { TabsSalesComponent } from './components/tabs-sales/tabs-sales.component';
import { TabsCustomerComponent } from './components/tabs-customer/tabs-customer.component';
import { TabsSupportComponent } from './components/tabs-support/tabs-support.component';
import { TabsMarketingComponent } from './components/tabs-marketing/tabs-marketing.component';

@Component({
    selector: 'app-analytics-and-reporting',
    standalone: false,
    templateUrl: './analytics-and-reporting.component.html',
    styleUrl: './analytics-and-reporting.component.scss'
})
export class AnalyticsAndReportingComponent implements OnInit {
    salesList$!: Observable<SalesOpportunity[]>;
    customerList$!: Observable<CustomerProfile[]>;
    customerSupportList$!: Observable<SupportTicket[]>;

    constructor(
        private readonly salesService: SalesService,
        private readonly customersService: CustomersService,
        private readonly customerSupportService: CustomerSupportService
    ) {}

    ngOnInit(): void {
        this.salesList$ = this.salesService.getAllSales();
        this.customerList$ = this.customersService.getCustomers();
        this.customerSupportList$ = this.customerSupportService.getAllTickets();
    }

    tabs = [
        { label: 'Customer Data Management', content: TabsCustomerComponent },
        { label: 'Sales Automation', content: TabsSalesComponent },
        { label: 'Customer Support', content: TabsSupportComponent },
        { label: 'Marketing Automation', content: TabsMarketingComponent }
    ];
}
