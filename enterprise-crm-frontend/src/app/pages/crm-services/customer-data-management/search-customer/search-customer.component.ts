import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Subject, Subscription, debounceTime, distinctUntilChanged } from 'rxjs';
import { CustomerProfile } from '../../../../models/CustomerProfile';

@Component({
    selector: 'app-search-customer',
    standalone: false,
    templateUrl: './search-customer.component.html',
    styleUrl: './search-customer.component.scss'
})
export class SearchCustomerComponent {
    @Input() customers: CustomerProfile[] = [];
    @Output() searchChanged = new EventEmitter<CustomerProfile[]>();
    @Output() searchTermChange = new EventEmitter<string>();

    searchTerm: string = '';
    searchTermChanged$ = new Subject<string>();
    private subscription: Subscription | undefined;

    constructor() {}

    ngOnInit(): void {
        this.subscription = this.searchTermChanged$.subscribe((searchTerm) => {
            this.performSearch(searchTerm);
            this.searchTermChange.emit(searchTerm);
        });
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    onSearchInputChange(event: Event): void {
        const value = (event.target as HTMLInputElement).value;
        this.searchTermChanged$.next(value);
    }

    performSearch(searchText: string): void {
        this.searchTerm = searchText;
        this.searchChanged.emit(this.filterCustomers(searchText));
    }

    filterCustomers(searchText: string): CustomerProfile[] {
        if (!searchText) {
            return this.customers;
        }
        const searchTextLower = searchText.toLowerCase();
        return this.customers.filter((customer) =>
            Object.values(customer).some((value) => {
                if (typeof value === 'string') {
                    return value.toLowerCase().includes(searchTextLower);
                }
                if (typeof value === 'number') {
                    return value.toString().includes(searchTextLower);
                }
                if (typeof value === 'object' && value !== null) {
                    return Object.values(value).some((nestedValue) => {
                        if (typeof nestedValue === 'string') {
                            return nestedValue.toLowerCase().includes(searchTextLower);
                        }
                        if (typeof nestedValue === 'number') {
                            return nestedValue.toString().includes(searchTextLower);
                        }
                        return false;
                    });
                }
                return false;
            })
        );
    }
}
