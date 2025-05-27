import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupportTicket } from '../../../../models/SupportTicket';
import { environment } from '../../../../../environments/environment.development';

@Injectable({
    providedIn: 'root'
})
export class CustomerSupportService {
    private apiUrl = environment.apiUrl + '/support';

    constructor(private readonly http: HttpClient) {}

    public createTicket(supportTicket: SupportTicket) {
        const agentId = '30' + (Math.random() * 10).toFixed(0);
        supportTicket.assignedAgent = agentId;
        return this.http.post<SupportTicket>(this.apiUrl, supportTicket);
    }

    public getTicketById(ticketId: number | null) {
        return this.http.get<SupportTicket>(this.apiUrl + `/${ticketId}`);
    }

    public updateTicket(supportTicket: SupportTicket) {
        return this.http.patch<SupportTicket>(this.apiUrl + '/' + supportTicket.ticketID + `/status?status=${supportTicket.status}`, null);
    }

    public deleteTicket(ticketID: string) {
        return this.http.delete(this.apiUrl + '/' + ticketID, { responseType: 'text' });
    }

    public getAllTickets() {
        return this.http.get<SupportTicket[]>(this.apiUrl);
    }
}
