import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, Subscription, takeUntil } from 'rxjs';
import { CustomerProfile } from '../../../../models/CustomerProfile';
import { CustomersService } from '../service/customers.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
    selector: 'app-customer-list',
    templateUrl: './customer-list.component.html',
    styleUrl: './customer-list.component.scss',
    standalone: false
})
export class CustomerListComponent implements OnInit, OnDestroy {
    allCustomers: CustomerProfile[] = [];
    filteredCustomers: CustomerProfile[] = [];
    searchBookId: string = '';
    customers: CustomerProfile[] = [];
    loading = true;
    error: string | null = null;
    private sub?: Subscription;
    private unsubscribe$ = new Subject<void>();

    // Pagination
    pageSize = 5;
    currentPage = 0;
    totalPages: number = 0;
    totalItems: number = 0;

    // Delete Confirmation
    showDeleteModal = false;
    selectedCustomerId: number | null = null;
    deleteErrorMessage: string | null = null;
    deleteSuccessMessage: string | null = null;

    constructor(
        private customerService: CustomersService,
        private cdr: ChangeDetectorRef,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.loadTotalPages();
        this.loadAllCustomers();
    }

    ngOnDestroy(): void {
        this.sub?.unsubscribe();
        this.unsubscribe$.next();
        this.unsubscribe$.complete();
    }

    loadTotalPages() {
        this.customerService
            .getCustomers()
            .pipe(takeUntil(this.unsubscribe$))
            .subscribe({
                next: (data) => {
                    this.totalPages = Math.ceil(data.length / this.pageSize) > 0 ? Math.ceil(data.length / this.pageSize) : 1;
                    this.totalItems = data.length;
                    console.log('Initial Total Pages:', this.totalPages);
                }
            });
    }

    loadAllCustomers() {
        this.customerService
            .getCustomers()
            .pipe(takeUntil(this.unsubscribe$))
            .subscribe(
                (data) => {
                    this.allCustomers = data;
                    this.filteredCustomers = data; // Initialize filteredCustomers
                    this.updatePagedCustomers();
                    this.loading = false;
                },
                (error) => {
                    this.loading = false;
                    this.error = 'Failed to load Customers';
                }
            );
    }

    updatePagedCustomers(): void {
        const startIndex = this.currentPage * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.customers = this.filteredCustomers.slice(startIndex, endIndex);
        this.cdr.detectChanges();
    }

    filterCustomers(searchText: string): void {
        this.searchBookId = searchText;
        this.currentPage = 0;

        if (!this.searchBookId) {
            this.filteredCustomers = [...this.allCustomers];
        } else {
            this.filteredCustomers = this.allCustomers.filter((customer) =>
                Object.values(customer).some((value) => {
                    if (typeof value === 'string') {
                        return value.toLowerCase().includes(searchText.toLowerCase());
                    }
                    if (typeof value === 'number') {
                        return value.toString().includes(searchText.toLowerCase());
                    }
                    if (typeof value === 'object' && value !== null) {
                        return Object.values(value).some((nestedValue) => {
                            if (typeof nestedValue === 'string') {
                                return nestedValue.toLowerCase().includes(searchText.toLowerCase());
                            }
                            if (typeof nestedValue === 'number') {
                                return nestedValue.toString().includes(searchText.toLowerCase());
                            }
                            return false;
                        });
                    }
                    return false;
                })
            );
        }
        this.totalItems = this.filteredCustomers.length;
        this.totalPages = Math.ceil(this.totalItems / this.pageSize) > 0 ? Math.ceil(this.totalItems / this.pageSize) : 1;
        this.updatePagedCustomers();
    }

    get pages(): number[] {
        const pageNumbers = [];
        for (let i = 0; i < this.totalPages; i++) {
            // Start from 0 to match currentPage
            pageNumbers.push(i);
        }
        return pageNumbers;
    }

    changePage(page: number): void {
        if (page >= 0 && page < this.totalPages) {
            this.currentPage = page;
            this.updatePagedCustomers();
        }
    }

    openDeleteModal(customerId: number | null): void {
        this.selectedCustomerId = customerId;
        this.showDeleteModal = true;
        this.deleteErrorMessage = null;
        this.deleteSuccessMessage = null;
    }

    closeDeleteModal(): void {
        this.showDeleteModal = false;
        this.selectedCustomerId = null;
    }

    handleDeleteSuccessful(): void {
        this.loadAllCustomers();
        this.showDeleteModal = false;
        this.selectedCustomerId = null;
        this.deleteSuccessMessage = 'Customer Deleted Successfully';
    }

    handleDeleteFailed(error: HttpErrorResponse): void {
        this.deleteErrorMessage = error.message || 'Failed to delete customer.';
        this.loading = false;
    }

    onCustomersSearchChanged(filteredCustomers: CustomerProfile[]): void {
        this.filteredCustomers = filteredCustomers;
        this.currentPage = 0;
        this.updatePagedCustomers();
    }

    onSearchTermChanged(searchTerm: string): void {
        this.searchBookId = searchTerm;
        this.filterCustomers(searchTerm);
    }

    handleFilteredCustomers(filteredList: CustomerProfile[]) {
        this.filteredCustomers = filteredList;
        this.currentPage = 0;
        this.updatePagedCustomers();
    }

    updateBtn(customerId: number | null) {
        console.log(customerId);
        this.customerService.setCustomerId(customerId);
        this.router.navigate([`/pages/services/customer-data-management/update-customer/${customerId}`]);
    }
}
