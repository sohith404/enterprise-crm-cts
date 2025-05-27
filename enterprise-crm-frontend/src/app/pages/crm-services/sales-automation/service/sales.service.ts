import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { SalesOpportunity } from '../../../../models/SalesOpportunity';
import { environment } from '../../../../../environments/environment.development';

@Injectable({
    providedIn: 'root'
})
export class SalesService {
    private salesSubject = new BehaviorSubject<SalesOpportunity[]>([]);
    salesList$: Observable<SalesOpportunity[]> = this.salesSubject.asObservable();

    constructor(private readonly httpClient: HttpClient) {
        this.loadSales(); // Initial data load
    }

    private loadSales() {
        this.httpClient.get<SalesOpportunity[]>(environment.apiUrl + '/sales-opportunity').subscribe((sales) => {
            this.salesSubject.next(sales); // Push initial sales data
        });
    }

    create(salesOpportunity: { customerID: string; estimatedValue: number; closingDate: string; salesStage: string }) {
        return this.httpClient.post<SalesOpportunity>(environment.apiUrl + '/sales-opportunity', salesOpportunity).pipe(
            tap((newLead) => {
                this.salesSubject.next([...this.salesSubject.value, newLead]); // Automatically update pipeline
            })
        );
    }
    refreshSalesList() {
        this.httpClient.get<SalesOpportunity[]>(environment.apiUrl + '/sales-opportunity').subscribe((sales) => {
            this.salesSubject.next(sales); // 🔄 Push new updates dynamically
        });
    }

    updateSales(salesOpportunity: SalesOpportunity) {
        return this.httpClient.put<SalesOpportunity>(environment.apiUrl + '/sales-opportunity/' + salesOpportunity.opportunityID, salesOpportunity).pipe(
            tap((updatedLead) => {
                const updatedSales = this.salesSubject.value.map((sale) => (sale.opportunityID === updatedLead.opportunityID ? updatedLead : sale));
                this.salesSubject.next(updatedSales); // Ensure updates are reflected live
            })
        );
    }

    updateNotificationSchedule(cron: string) {
        return this.httpClient.post<{ id: string; taskName: string; cronExpression: string }>(environment.apiUrl + '/sales-opportunity/setReminderSchedule', { cronExpression: cron });
    }

    updateClosingSchedule(cron: string) {
        return this.httpClient.post<{ id: string; taskName: string; cronExpression: string }>(environment.apiUrl + '/sales-opportunity/setClosingSchedule', { cronExpression: cron });
    }

    getAllSales() {
        return this.httpClient.get<SalesOpportunity[]>(environment.apiUrl + '/sales-opportunity');
    }
}
