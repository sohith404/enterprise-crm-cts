import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MarketingAutomationComponent } from '../../../pages/crm-services/marketing-automation/marketing-automation.component';
import { MarketingAutomationService } from '../../../pages/crm-services/marketing-automation/service/marketing-automation.service';
import { DemoComponent } from '../../../pages/crm-services/marketing-automation/components/demo/demo.component';
import { CreateCampaignComponent } from '../../../pages/crm-services/marketing-automation/components/create-campaign/create-campaign.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ListCampaignComponent } from '../../../pages/crm-services/marketing-automation/components/list-campaign/list-campaign.component';
import { GetCampaignByIdComponent } from '../../../pages/crm-services/marketing-automation/components/get-campaign-by-id/get-campaign-by-id.component';
import { DeleteCampaignComponent } from '../../../pages/crm-services/marketing-automation/components/delete-campaign/delete-campaign.component';
import { ReachAnalysisComponent } from '../../../pages/crm-services/marketing-automation/components/reach-analysis/reach-analysis.component';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DatePicker } from 'primeng/datepicker';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@NgModule({
    declarations: [MarketingAutomationComponent, DemoComponent, CreateCampaignComponent, ListCampaignComponent, GetCampaignByIdComponent, DeleteCampaignComponent, ReachAnalysisComponent],
    imports: [CommonModule, ReactiveFormsModule, RouterModule, FormsModule, ButtonModule, InputTextModule, ConfirmDialogModule, DatePicker],
    providers: [MarketingAutomationService]
})
export class MarketingAutomationModule {}
