// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-delete-ticket',
//   standalone: false,
//   templateUrl: './delete-ticket.component.html',
//   styleUrl: './delete-ticket.component.scss'
// })
// export class DeleteTicketComponent {

// }

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { CustomerSupportService } from '../../service/customer-support.service';

@Component({
    selector: 'app-delete-ticket',
    standalone: false,
    templateUrl: './delete-ticket.component.html',
    styleUrls: ['./delete-ticket.component.scss']
})
export class DeleteTicketComponent implements OnInit {
    @Input() selectedTicketId: string | null = null;
    @Output() close = new EventEmitter<void>();
    @Output() deleteSuccessful = new EventEmitter<void>();

    errorMessage: string | null = null;
    successMessage: string | null = null;
    loading: boolean = false;

    constructor(private supportService: CustomerSupportService) {}

    ngOnInit(): void {
        if (this.selectedTicketId === null) {
            this.errorMessage = 'Ticket ID is null. Cannot delete.';
        }
    }

    confirmDelete(): void {
        if (this.selectedTicketId === null) {
            this.errorMessage = 'No Ticket ID provided to delete.';
            return;
        }

        this.loading = true;
        this.errorMessage = null;
        this.successMessage = null;

        this.supportService.deleteTicket(this.selectedTicketId).subscribe({
            next: (res: string) => {
                this.successMessage = res;
                this.deleteSuccessful.emit();
                setTimeout(() => this.onClose(), 1000);
            },
            error: (error: HttpErrorResponse) => {
                this.errorMessage = error.error.message;
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
