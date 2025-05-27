import { Component, OnInit } from '@angular/core';
import { Campaign } from '../../../../../models/Campaign';
import { MarketingAutomationService } from '../../service/marketing-automation.service';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-list-campaign',
    standalone: false,
    templateUrl: './list-campaign.component.html',
    styleUrl: './list-campaign.component.scss'
})
export class ListCampaignComponent implements OnInit {
    searchText = '';
    campaigns: Campaign[] = [];
    filteredCampaigns: Campaign[] = [];
    searched = false;

    // Pagination properties
    pageSize = 6; // Number of campaigns to display per page
    currentPage = 1;
    totalPages = 0;
    pagedCampaigns: Campaign[] = [];

    today: string = new Date().toISOString().split('T')[0];
    selectedCampaignType = '';
    selectedCampaignStatus = '';

    constructor(private marketingService: MarketingAutomationService) {}

    ngOnInit(): void {
        this.loadCampaigns();
    }

    loadCampaigns(): void {
        this.marketingService.getCampaigns().subscribe((data) => {
            this.campaigns = data.reverse();
            this.filterCampaigns(); // Initial filtering
        });
    }

    formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString();
    }

    calculateStatus(campaign: Campaign): string {
        const startDate = new Date(campaign.startDate);
        const endDate = new Date(campaign.endDate);
        const today = new Date(this.today);

        if (today < startDate) {
            return 'Upcoming';
        } else if (today >= startDate && today <= endDate) {
            return 'Active';
        } else {
            return 'Completed';
        }
    }

    getCampaignClass(campaign: Campaign): { [key: string]: boolean } {
        return {
            active: this.calculateStatus(campaign) === 'Active',
            completed: this.calculateStatus(campaign) === 'Completed',
            upcoming: this.calculateStatus(campaign) === 'Upcoming'
        };
    }

    filterCampaigns(): void {
        let filtered = [...this.campaigns];

        if (this.selectedCampaignType) {
            filtered = filtered.filter((campaign) => campaign.type === this.selectedCampaignType);
        }

        if (this.selectedCampaignStatus) {
            filtered = filtered.filter((campaign) => this.calculateStatus(campaign) === this.selectedCampaignStatus);
        }

        if (this.searchText) {
            const lowerSearchText = this.searchText.toLowerCase();
            filtered = filtered.filter((campaign) => campaign.campaignID?.toString().toLowerCase().includes(lowerSearchText));
            this.searched = true;
        } else {
            this.searched = false;
        }

        this.filteredCampaigns = filtered;
        this.updatePagination();
    }

    onSubmit(): void {
        this.currentPage = 1;
        this.filterCampaigns();
    }

    // Pagination methods
    updatePagination(): void {
        this.totalPages = Math.ceil(this.filteredCampaigns.length / this.pageSize);
        this.paginateCampaigns();
    }

    paginateCampaigns(): void {
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.pagedCampaigns = this.filteredCampaigns.slice(startIndex, endIndex);
    }

    goToPage(pageNumber: number): void {
        if (pageNumber >= 1 && pageNumber <= this.totalPages) {
            this.currentPage = pageNumber;
            this.paginateCampaigns();
        }
    }

    nextPage(): void {
        this.goToPage(this.currentPage + 1);
    }

    previousPage(): void {
        this.goToPage(this.currentPage - 1);
    }

    onCampaignTypeChange(event: any): void {
        this.selectedCampaignType = event.target.value;
        this.currentPage = 1;
        this.filterCampaigns();
    }

    onCampaignStatusChange(event: any): void {
        this.selectedCampaignStatus = event.target.value;
        this.currentPage = 1;
        this.filterCampaigns();
    }

    getUniqueCampaignTypes(): string[] {
        return [...new Set(this.campaigns.map((campaign) => campaign.type))];
    }

    getUniqueCampaignStatuses(): string[] {
        return [...new Set(this.campaigns.map((campaign) => this.calculateStatus(campaign)))];
    }
}
