import { Component } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { Observable, of } from 'rxjs';
import { SupportTicket } from '../../../../../models/SupportTicket';
import { CustomerSupportService } from '../../../customer-support/service/customer-support.service';

@Component({
    selector: 'app-tabs-support',
    standalone: false,
    templateUrl: './tabs-support.component.html',
    styleUrl: './tabs-support.component.scss'
})
export class TabsSupportComponent {
    constructor(
        private readonly currencyPipe: CurrencyPipe,
        private readonly supportService: CustomerSupportService
    ) {}

    totalTickets!: number;
    closedTickets!: number;
    openTickets!: number;
    totalAgents!: number;

    statistics: {
        title: string;
        count: string;
        iconBg: string;
        iconClass: string;
        closingLine: string;
        closingBg: string;
        footer: string;
    }[] = [];

    ngOnInit(): void {
        this.supportService.getAllTickets().subscribe({
            next: (tickets: SupportTicket[]) => {
                this.totalTickets = tickets.length;
                this.closedTickets = tickets.filter((ticket) => ticket.status == 'CLOSED').length;
                this.openTickets = this.totalTickets - this.closedTickets;

                const allAgents = new Set(...[tickets.map((ticket: SupportTicket) => ticket.assignedAgent)]);
                this.totalAgents = allAgents.size;

                this.statistics = [
                    {
                        title: 'Open Tickets',
                        count: this.openTickets.toString(),
                        iconBg: 'bg-blue-100',
                        iconClass: 'pi pi-users text-blue-500',
                        closingLine: `${this.totalTickets}`,
                        closingBg: 'text-green-500',
                        footer: 'Total tickets:'
                    },
                    {
                        title: 'Tickets/Agent',
                        count: (this.totalTickets / this.totalAgents).toFixed(2),
                        iconBg: 'bg-green-100',
                        iconClass: 'pi pi-indian-rupee text-green-500',
                        closingLine: `${this.totalAgents}`,
                        closingBg: 'text-green-500',
                        footer: 'Total agents:'
                    },
                    {
                        title: 'Closing Rate',
                        count: ((this.closedTickets / this.totalTickets) * 100).toFixed(2) + '%',
                        iconBg: 'bg-purple-100',
                        iconClass: 'pi pi-trophy text-purple-500',
                        closingLine: `${this.openTickets}`,
                        closingBg: 'text-red-500',
                        footer: 'Open tickets:'
                    }
                ];
            }
        });
    }
}
