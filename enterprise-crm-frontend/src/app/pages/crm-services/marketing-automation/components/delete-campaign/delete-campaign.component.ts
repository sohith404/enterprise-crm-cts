import { Component } from '@angular/core';
import { Campaign } from '../../../../../models/Campaign';
import { MarketingAutomationService } from '../../service/marketing-automation.service';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-delete-campaign',
    standalone: false,
    templateUrl: './delete-campaign.component.html',
    styleUrl: './delete-campaign.component.scss'
})
export class DeleteCampaignComponent {
    campaign: Campaign = new Campaign();
    submitted = false;
    result!: string;
    campaignIdToDelete!: number; // Property to hold the ID entered by the user
    constructor(
        private marketingService: MarketingAutomationService,
        private readonly messageService: MessageService
    ) {
        this.campaignIdToDelete = this.marketingService.getCampaignId();
    }
    delete() {
        this.marketingService.deleteCampaign(this.campaign).subscribe({
            next: () => this.messageService.add({ severity: 'info', summary: 'Success.', detail: 'Campaign Deleted Successfully' }),
            error: () => this.messageService.add({ severity: 'error', summary: 'Error.', detail: 'Some error occurred.' })
        });
        if (!this.campaignIdToDelete) {
            this.result = 'Invalid Campaign ID';
            return;
        }
        this.campaign = new Campaign();
    }
    onSubmit() {
        this.submitted = true;
        this.delete();
        this.result = 'Campaign Deleted Successfully';
    }
}
