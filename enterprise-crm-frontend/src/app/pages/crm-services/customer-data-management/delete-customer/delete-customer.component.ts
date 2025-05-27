import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { CustomerProfile } from '../../../../models/CustomerProfile';
import { CustomersService } from '../service/customers.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-delete-customer',
    standalone: false,
    templateUrl: './delete-customer.component.html',
    styleUrl: './delete-customer.component.scss'
})
export class DeleteCustomerComponent implements OnInit {
    @Input() selectedCustomerId: number | null = null;
    @Output() close = new EventEmitter<void>();
    @Output() deleteSuccessful = new EventEmitter<void>();

    errorMessage: string | null = null;
    successMessage: string | null = null;
    loading: boolean = false;

    constructor(
        private customersService: CustomersService,
        private readonly messageService: MessageService
    ) {}

    ngOnInit(): void {
        if (this.selectedCustomerId === null) {
            this.errorMessage = 'Customer Id is null. Cannot delete';
        }
    }

    confirmDelete(): void {
        if (this.selectedCustomerId === null) {
            this.errorMessage = 'No Customer ID provided to delete.';
            return;
        }

        this.loading = true;
        this.errorMessage = null;
        this.successMessage = null;

        this.customersService.deleteCustomer(this.selectedCustomerId).subscribe({
            next: (res: string) => {
                this.successMessage = res;
                this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Customer Deleted Successfully.' });
                this.deleteSuccessful.emit();
                setTimeout(() => this.onClose(), 1000);
            },

            error: (error: HttpErrorResponse) => {
                (this.errorMessage = error.error.message), this.messageService.add({ severity: 'error', summary: 'Success.', detail: 'Some Error Occurred.' });
                console.log(error.error.message);
                this.loading = false;
            },
            complete: () => (this.loading = false)
        });
    }

    onClose(): void {
        this.close.emit();
        this.errorMessage = '';
        this.successMessage = '';
    }
}
