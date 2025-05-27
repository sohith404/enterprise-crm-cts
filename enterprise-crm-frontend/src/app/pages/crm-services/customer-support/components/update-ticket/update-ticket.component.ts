import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SupportTicket } from '../../../../../models/SupportTicket';
import { CustomerSupportService } from '../../service/customer-support.service';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-update-ticket',
    standalone: false,
    templateUrl: './update-ticket.component.html',
    styleUrls: ['./update-ticket.component.scss']
})
export class UpdateTicketComponent implements OnInit {
    ticketForm!: FormGroup;
    loading = false;
    error: string | null = null;
    success = false;
    ticketId!: number | null;
    // issueDescription: string | null = null;
    ticketData!: SupportTicket;

    statusOptions: ('OPEN' | 'CLOSED')[] = ['OPEN', 'CLOSED'];

    constructor(
        private readonly fb: FormBuilder,
        private readonly supportService: CustomerSupportService,
        private readonly router: Router,
        private readonly route: ActivatedRoute,
        private readonly messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.ticketId = Number(this.route.snapshot.paramMap.get('id'));
        this.getTicketData();
        this.ticketForm = this.fb.group({
            ticketID: [{ value: '', disabled: true }, Validators.required],
            status: ['', Validators.required]
            // issueDescription: [{ string: ''}, Validators.required]
        });
    }

    getTicketData() {
        this.supportService.getTicketById(this.ticketId).subscribe((data) => {
            this.ticketData = data;
            this.ticketForm.patchValue(this.ticketData);
        });
    }

    onSubmit(): void {
        if (this.ticketForm.valid) {
            this.loading = true;
            this.error = null;
            this.success = false;

            let supportTicket: SupportTicket = this.ticketForm.getRawValue();
            supportTicket.ticketID = this.ticketId?.toString();

            this.supportService.updateTicket(supportTicket).subscribe({
                next: (response) => {
                    console.log('Ticket updated successfully:', response);
                    this.loading = false;
                    this.messageService.add({ severity: 'info', summary: 'Success.', detail: 'Ticket Updated Successfully.' });
                    this.success = true;
                    this.router.navigate(['pages/services/customer-support']);
                },
                error: (error) => {
                    this.error = error.message || 'Failed to update ticket.';
                    this.loading = false;
                    this.messageService.add({ severity: 'error', summary: 'Error.', detail: 'Some error occurred.' });
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
