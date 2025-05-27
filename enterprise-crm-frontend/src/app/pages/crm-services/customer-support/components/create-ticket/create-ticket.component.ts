import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SupportTicket } from '../../../../../models/SupportTicket';
import { CustomerSupportService } from '../../service/customer-support.service';
import { MessageService } from 'primeng/api';
import { Store } from '@ngrx/store';
import { Notification } from '../../../../../models/Notification';
import { addNotification } from '../../../../../store/notifications/notiffications.actions';

@Component({
    selector: 'app-create-ticket',
    standalone: false,
    templateUrl: './create-ticket.component.html',
    styleUrls: ['./create-ticket.component.scss']
})
export class CreateTicketComponent implements OnInit {
    ticketForm!: FormGroup;
    loading = false;
    error: string | null = null;
    success = false;

    constructor(
        private readonly fb: FormBuilder,
        private readonly supportService: CustomerSupportService,
        private readonly router: Router,
        private readonly messageService: MessageService,
        private readonly store: Store
    ) {}

    ngOnInit(): void {
        this.ticketForm = this.fb.group({
            customerID: ['', Validators.required],
            issueDescription: ['', Validators.required],
            status: ['OPEN']
        });
    }

    onSubmit(): void {
        if (this.ticketForm.valid) {
            this.loading = true;
            this.error = null;
            this.success = false;

            const supportTicket: SupportTicket = this.ticketForm.value;

            this.supportService.createTicket(supportTicket).subscribe({
                next: (response: any) => {
                    console.log('Ticket created successfully:', response);
                    this.loading = false;
                    this.messageService.add({
                        severity: 'info',
                        summary: 'Success.',
                        detail: 'Ticket Created Successfully.'
                    });
                    this.success = true;
                    const newNotification: Notification = {
                        heading: 'Customer Support',
                        description: `New ticket raised with ID: ${response.ticketID}.`,
                        time: new Date().toLocaleTimeString()
                    };
                    this.store.dispatch(addNotification({ notification: newNotification }));
                    this.router.navigate(['pages/services/customer-support']);
                },
                error: (error: { message: string }) => {
                    this.error = error.message || 'Failed to create ticket.';
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: 'Some error occurred.'
                    });
                    this.loading = false;
                    this.success = false;
                }
            });
        } else {
            this.markFormGroupTouched(this.ticketForm);
        }
    }

    markFormGroupTouched(formGroup: FormGroup) {
        (Object as any).values(formGroup.controls).forEach((control: any) => {
            control.markAsTouched();
            if (control.controls) {
                this.markFormGroupTouched(control);
            }
        });
    }
}
