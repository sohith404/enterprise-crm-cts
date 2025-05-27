import { ChangeDetectorRef, Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { SupportTicket } from '../../../../../models/SupportTicket';
import { CustomerSupportService } from '../../../customer-support/service/customer-support.service';

@Component({
    selector: 'app-supportmodule',
    standalone: false,
    templateUrl: './supportmodule.component.html',
    styleUrl: './supportmodule.component.scss'
})
export class SupportmoduleComponent implements OnInit {
    data: any;
    options: any;
    ticketData!: SupportTicket[];
    constructor(
        private readonly cd: ChangeDetectorRef,
        private readonly supportService: CustomerSupportService
    ) {}

    ngOnInit() {
        this.supportService.getAllTickets().subscribe({
            next: (tickets) => {
                this.ticketData = tickets;
                this.initChart();
            }
        });
    }

    initChart() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--p-text-color');
        const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
        const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');
        const greenColor = documentStyle.getPropertyValue('--p-green-500');
        const yellowColor = documentStyle.getPropertyValue('--p-yellow-500');

        // Process the ticket data to count tickets by status per customer
        const agentTicketCounts: { [key: string]: { open: number; closed: number; other: number } } = {};
        this.ticketData.forEach((ticket) => {
            const agentIdString = String(ticket.assignedAgent);
            if (!agentTicketCounts[agentIdString]) {
                agentTicketCounts[agentIdString] = { open: 0, closed: 0, other: 0 };
            }
            if (ticket.status === 'OPEN') {
                agentTicketCounts[agentIdString].open++;
            } else if (ticket.status === 'CLOSED') {
                agentTicketCounts[agentIdString].closed++;
            } else {
                agentTicketCounts[agentIdString].other++;
            }
        });

        const agentIDs = Object.keys(agentTicketCounts);
        const openTicketCounts = agentIDs.map((id) => agentTicketCounts[id].open);
        const closedTicketCounts = agentIDs.map((id) => agentTicketCounts[id].closed);

        this.data = {
            labels: agentIDs,
            datasets: [
                {
                    label: 'Open Tickets',
                    backgroundColor: yellowColor,
                    borderColor: yellowColor,
                    data: openTicketCounts
                },
                {
                    label: 'Closed Tickets',
                    backgroundColor: greenColor,
                    borderColor: greenColor,
                    data: closedTicketCounts
                }
            ]
        };

        this.options = {
            maintainAspectRatio: false,
            aspectRatio: 0.6,
            scales: {
                x: {
                    stacked: true, // Stack the bars for different statuses
                    title: {
                        display: true,
                        text: 'Agent ID',
                        color: textColorSecondary
                    },
                    ticks: {
                        color: textColorSecondary
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false
                    }
                },
                y: {
                    stacked: true, // Stack the bars for different statuses
                    title: {
                        display: true,
                        text: 'Number of Tickets',
                        color: textColorSecondary
                    },
                    ticks: {
                        color: textColorSecondary,
                        beginAtZero: true,
                        precision: 0
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: textColor
                    }
                }
            }
        };
        this.cd.markForCheck();
    }
}
