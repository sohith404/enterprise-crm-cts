import { environment } from './../../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CustomerProfile } from '../../../../models/CustomerProfile';
import { SupportTicket } from '../../../../models/SupportTicket';
import { SalesOpportunity } from '../../../../models/SalesOpportunity';
import { AllReports, CustomerReport, MarketingReport, SalesReport, SupportReport } from '../../../../models/Analytics';

@Injectable({
    providedIn: 'root'
})
export class AnalyticsService {
    constructor(private readonly httpClient: HttpClient) {}

    getCustomers() {
        return this.httpClient.get<CustomerProfile[]>(environment.apiUrl + '/customers');
    }

    getSupport() {
        return this.httpClient.get<SupportTicket[]>(environment.apiUrl + '/supportTicket');
    }

    getSales() {
        return this.httpClient.get<SalesOpportunity[]>(environment.apiUrl + '/sales');
    }

    generateCustomerReport() {
        return this.httpClient.post<CustomerReport>(environment.apiUrl + `/analytics/customers`, null);
    }
    generateMarketingReport() {
        return this.httpClient.post<MarketingReport>(environment.apiUrl + `/analytics/marketingCampaigns`, null);
    }
    generateSalesReport() {
        return this.httpClient.post<SalesReport>(environment.apiUrl + `/analytics/sales`, null);
    }
    generateSupportReport() {
        return this.httpClient.post<SupportReport>(environment.apiUrl + `/analytics/supportTickets`, null);
    }

    getAllReports() {
        return this.httpClient.get<AllReports>(environment.apiUrl + `/analytics`);
    }
}
