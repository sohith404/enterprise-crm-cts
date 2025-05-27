import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { CustomerProfile } from '../../../../models/CustomerProfile';

interface Filter {
    interest?: string;
    region?: string;
    purchasingHabits?: string;
}
@Component({
    selector: 'app-filter-customer',
    standalone: false,
    templateUrl: './filter-customer.component.html',
    styleUrl: './filter-customer.component.scss'
})
export class FilterCustomerComponent {
    @Input() customers: CustomerProfile[] = [];
    @Output() filteredCustomers = new EventEmitter<CustomerProfile[]>();

    // Dropdown state
    isFilterOpen = false;
    activeSection: 'interest' | 'region' | 'purchasingHabits' | null = null;

    // Filter values
    interestOptions: string[] = [
        'MUSIC',
        'TECH',
        'FASHION',
        'SPORTS',
        'ACADEMICS',
        'HOME_APPLIANCES',
        'GIFT',
        'ESSENTIALS',
        'TRAVEL',
        'FOOD_AND_DRINK',
        'HEALTH_AND_WELLNESS',
        'ART_AND_CRAFTS',
        'GAMING',
        'AUTOMOTIVE',
        'PETS',
        'GARDENING',
        'FINANCE',
        'BEAUTY_AND_COSMETICS',
        'BOOKS_AND_LITERATURE',
        'MOVIES_AND_TV',
        'PHOTOGRAPHY',
        'DIY_AND_HOME_IMPROVEMENT',
        'OUTDOOR_ACTIVITIES',
        'SOCIAL_MEDIA',
        'FITNESS',
        'ENVIRONMENT',
        'SCIENCE_AND_TECHNOLOGY'
    ];

    regionOptions: string[] = [
        'NORTH_AMERICA',
        'LATIN_AMERICA',
        'ASIAN_PACIFIC',
        'MIDDLE_EAST_AND_AFRICA',
        'CENTRAL_AMERICA',
        'EUROPE',
        'NORTH',
        'EASTERN_EUROPE',
        'WESTERN_EUROPE',
        'NORTHERN_EUROPE',
        'SOUTHERN_EUROPE',
        'SOUTHEAST_ASIA',
        'SOUTH_ASIA',
        'EAST_ASIA',
        'OCEANIA',
        'SUB_SAHARAN_AFRICA'
    ];

    purchasingHabitsOptions: string[] = ['NEW', 'FREQUENT', 'SPARSE', 'REGULAR'];

    // Selected filter
    selectedFilter: Filter = {};

    constructor() {}

    ngOnInit(): void {}

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['customers']) {
            this.applyFilters();
        }
    }

    // Toggle main filter dropdown
    toggleFilter() {
        this.isFilterOpen = !this.isFilterOpen;
        this.activeSection = null; // Close any open sub-dropdown
    }

    // Open sub-dropdown
    openSection(section: 'interest' | 'region' | 'purchasingHabits') {
        this.activeSection = section;
    }

    // Select a filter value
    selectFilterValue(section: keyof Filter, value: string) {
        this.selectedFilter[section] = value;
        this.isFilterOpen = false; // Close the dropdown
        this.activeSection = null;
        this.applyFilters();
    }

    // Clear a filter
    clearFilter(section: keyof Filter) {
        delete this.selectedFilter[section];
        this.applyFilters();
    }

    // Apply filters
    applyFilters() {
        let filtered = [...this.customers]; // Start with a copy

        if (this.selectedFilter.interest) {
            filtered = filtered.filter((c) => c.segmentationData.Interest === this.selectedFilter.interest);
        }
        if (this.selectedFilter.region) {
            filtered = filtered.filter((c) => c.segmentationData.Region === this.selectedFilter.region);
        }
        if (this.selectedFilter.purchasingHabits) {
            filtered = filtered.filter((c) => c.segmentationData['Purchasing Habits'] === this.selectedFilter.purchasingHabits);
        }

        this.filteredCustomers.emit(filtered);
    }
}
