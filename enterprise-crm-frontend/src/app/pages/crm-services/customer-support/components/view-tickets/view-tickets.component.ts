import { SupportTicket } from './../../../../../models/SupportTicket';
import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CustomerSupportService } from '../../service/customer-support.service';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
    selector: 'app-view-tickets',
    standalone: false,
    templateUrl: './view-tickets.component.html',
    styleUrl: './view-tickets.component.scss'
})
export class ViewTicketsComponent implements OnInit, OnChanges {
    searchedText: string = '';
    selectedStatus: string = '';
    showStatusFilterOptions: boolean = false;

    @Input({ required: true }) customers: SupportTicket[] | null = [];
    pagedCustomers: SupportTicket[] = [];
    filteredTickets: SupportTicket[] = [];
    pageSize = 5;
    currentPage = 1;
    totalPages = 1;

    private searchSubject = new Subject<string>();

    constructor(
        private router: Router,
        private customerSupportService: CustomerSupportService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) {}

    ngOnInit(): void {
        this.resetFilters();
        this.updatePagedCustomers();
        this.setupSearchDebounce();
    }

    ngOnChanges(): void {
        this.resetFilters();
        this.updatePagedCustomers();
    }

    setupSearchDebounce(): void {
        this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe((searchText) => {
            this.applyFilters(searchText, this.selectedStatus);
        });
    }

    handleSearchInput(event: Event): void {
        const searchText = (event.target as HTMLInputElement).value;
        this.searchedText = searchText;
        this.searchSubject.next(searchText);
    }

    toggleStatusFilter(): void {
        this.showStatusFilterOptions = !this.showStatusFilterOptions;
    }

    filterByStatus(status: string): void {
        this.selectedStatus = status;
        this.applyFilters(this.searchedText, status);
        this.showStatusFilterOptions = false;
    }

    confirmDelete(event: Event, id: string) {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Do you want to delete this ticket?',
            header: 'Danger Zone',
            icon: 'pi pi-info-circle',
            rejectLabel: 'Cancel',
            rejectButtonProps: {
                label: 'Cancel',
                severity: 'secondary',
                outlined: true
            },
            acceptButtonProps: {
                label: 'Delete',
                severity: 'danger'
            },

            accept: () => {
                this.handleDelete(id);
            },
            reject: () => {
                this.messageService.add({ severity: 'info', summary: 'Closed', detail: 'You have canceled' });
            }
        });
    }

    handleDelete(id: string) {
        this.pagedCustomers = this.pagedCustomers.filter((ticket) => ticket.ticketID !== id);
        this.customerSupportService.deleteTicket(id).subscribe({
            next: () => {
                this.messageService.add({ severity: 'info', summary: 'Confirmed.', detail: 'Ticket deleted.' });
            },
            error: () => {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Some Error Occured.' });
            }
        });
    }

    updatePagedCustomers(): void {
        this.totalPages = this.filteredTickets ? Math.ceil(this.filteredTickets.length / this.pageSize) : 0;
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.pagedCustomers = this.filteredTickets ? this.filteredTickets.slice(startIndex, endIndex) : [];
        console.log(this.pagedCustomers);
    }

    applyFilters(searchText: string, statusFilter: string): void {
        if (this.customers) {
            this.filteredTickets = this.customers.filter((ticket) => {
                const searchTextMatch =
                    ticket.ticketID?.toString().toLowerCase().includes(searchText.toLowerCase()) ||
                    ticket.customerID?.toString().toLowerCase().includes(searchText.toLowerCase()) ||
                    ticket.issueDescription?.toLowerCase().includes(searchText.toLowerCase()) ||
                    ticket.assignedAgent?.toString().toLowerCase().includes(searchText.toLowerCase()) ||
                    ticket.status?.toLowerCase().includes(searchText.toLowerCase());

                const statusMatch = !statusFilter || ticket.status?.toLowerCase() === statusFilter.toLowerCase();

                return searchTextMatch && statusMatch;
            });
        } else {
            this.filteredTickets = [];
        }
        this.currentPage = 1;
        this.updatePagedCustomers();
        console.log('Filtered Tickets:', this.filteredTickets);
    }

    resetFilters(): void {
        this.filteredTickets = this.customers ? [...this.customers] : [];
        this.selectedStatus = '';
    }

    changePage(page: number): void {
        if (page >= 1 && page <= this.totalPages) {
            this.currentPage = page;
            this.updatePagedCustomers();
        }
    }

    getPages(): number[] {
        const pages = [];
        for (let i = 1; i <= this.totalPages; i++) {
            pages.push(i);
        }
        return pages;
    }
}
