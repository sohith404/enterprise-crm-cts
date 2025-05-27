import { Component, Input, OnInit } from '@angular/core';
import { SupportTicket } from '../../../../../models/SupportTicket';
import { Observable } from 'rxjs';
import { MenuItem, MessageService } from 'primeng/api';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomerSupportService } from './../../service/customer-support.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-customer-support-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
    standalone: false
})
export class CustomerSupportHeaderComponent implements OnInit {
    constructor() {}

    ngOnInit(): void {}

    // @Input({ required: true }) title!: string;

    // constructor(
    //   private readonly fb: FormBuilder,
    //   private readonly customerSupportService: CustomerSupportService,
    //   private readonly messageService: MessageService
    // ) {}

    // ngOnInit(): void {
    //   this.newTicketForm = this.fb.group({
    //     customerId: ['', [Validators.required, Validators.min(1)]],
    //     issueDescription: ['', [Validators.required, Validators.minLength(10)]],
    //     assignedAgent: ['', [Validators.required]],
    //   });

    //   this.items = [
    //     {
    //       icon: 'pi pi-calendar',
    //       label: 'Schedule Ticket Closing Time',
    //       command: () => {
    //         this.showDialog1();
    //       },
    //     },
    //     {
    //       icon: 'pi pi-clock',
    //       label: 'Schedule Notification Time',
    //       command: () => {
    //         this.showDialog2();
    //       },
    //     },
    //     {
    //       icon: 'pi pi-plus',
    //       label: 'Create New Ticket',
    //       command: () => {
    //         this.showDialog3();
    //       },
    //     },
    //   ];

    // }

    // activeTickets!: number;
    // resolvedTickets!: number;
    // pendingTickets!: number;
    // items!: MenuItem[];
    // newTicketForm!: FormGroup;

    // visible1: boolean = false;
    // visible2: boolean = false;
    // visible3: boolean = false;

    // isLoading = false;

    // timeToDailyCron(timeString: string) {
    //   const date = new Date(timeString);
    //   const minutes = date.getMinutes();
    //   const hours = date.getHours();
    //   return `* ${minutes} ${hours} * * * `;
    // }

    // showDialog1() {
    //   this.visible1 = true;
    // }

    // showDialog2() {
    //   this.visible2 = true;
    // }

    // showDialog3() {
    //   this.visible3 = true;
    // }

    // showToast(toast: { severity: string, summary: string, message: string }) {
    //   this.messageService.add({ severity: toast.severity, summary: toast.summary, detail: toast.message });
    // }

    // handleTicketSumbit() {
    //   this.isLoading = true;
    //   this.customerSupportService.createSupportTicket({
    //     customerID: this.newTicketForm.get('customerId')?.value,
    //     issueDescription: this.newTicketForm.get('issueDescription')?.value,
    //     assignedAgent: this.newTicketForm.get('assignedAgent')?.value,
    //     status: "OPEN",
    //   }).subscribe({
    //     next: (ticket: SupportTicket) => {
    //       this.showToast({ severity: 'success', summary: 'Saved', message: `Ticket created successfully with id: ${ticket.ticketID}` });
    //     },
    //     error: (error: HttpErrorResponse) => {
    //       this.showToast({ severity: 'error', summary: 'Error', message: error.error.message });
    //       this.isLoading = false;
    //       this.newTicketForm.reset();
    //     },
    //     complete: () => {
    //       this.isLoading = false;
    //       this.visible3 = false;
    //       this.newTicketForm.reset();
    //     },
    //   });
    // }

    // get customerIdErrorMessage(): string {
    //   const control = this.newTicketForm.get('customerId');
    //   if (control?.hasError('required')) {
    //     return 'Customer ID is required.';
    //   }
    //   if (control?.hasError('min')) {
    //     return 'Customer ID must be valid';
    //   }
    //   return '';
    // }

    // get issueDescriptionErrorMessage(): string {
    //   const control = this.newTicketForm.get('issueDescription');
    //   if (control?.hasError('required')) {
    //     return 'Issue description is required.';
    //   }
    //   if (control?.hasError('minLength')) {
    //     return 'Issue description must be at least 10 characters long.';
    //   }
    //   return '';
    // }

    // get assignedAgentErrorMessage(): string {
    //   const control = this.newTicketForm.get('assignedAgent');
    //   if (control?.hasError('required')) {
    //     return 'Assigned agent is required.';
    //   }
    //   return '';
    // }
}
