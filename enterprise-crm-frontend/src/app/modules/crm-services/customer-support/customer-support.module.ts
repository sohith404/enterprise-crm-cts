import { CreateTicketComponent } from './../../../pages/crm-services/customer-support/components/create-ticket/create-ticket.component';
import { ViewTicketsComponent } from './../../../pages/crm-services/customer-support/components/view-tickets/view-tickets.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerSupportComponent } from '../../../pages/crm-services/customer-support/customer-support.component';
import { CustomerSupportService } from '../../../pages/crm-services/customer-support/service/customer-support.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateTicketComponent } from '../../../pages/crm-services/customer-support/components/update-ticket/update-ticket.component';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { Dialog } from 'primeng/dialog';
import { DatePickerModule } from 'primeng/datepicker';
import { IftaLabelModule } from 'primeng/iftalabel';
import { FloatLabel } from 'primeng/floatlabel';
import { Tooltip } from 'primeng/tooltip';
import { SpeedDial } from 'primeng/speeddial';
import { FluidModule } from 'primeng/fluid';
import { CustomerSupportHeaderComponent } from '../../../pages/crm-services/customer-support/components/header/header.component';
import { MessageService } from 'primeng/api';
import { RouterModule } from '@angular/router';
import { DeleteTicketComponent } from '../../../pages/crm-services/customer-support/components/delete-ticket/delete-ticket.component';
import { ConfirmationService } from 'primeng/api';
import { ConfirmDialog } from 'primeng/confirmdialog';

@NgModule({
    declarations: [CustomerSupportComponent, ViewTicketsComponent, CreateTicketComponent, CustomerSupportHeaderComponent, UpdateTicketComponent, DeleteTicketComponent],
    imports: [CommonModule, ReactiveFormsModule, ButtonModule, MenuModule, Dialog, DatePickerModule, IftaLabelModule, FloatLabel, Tooltip, SpeedDial, FluidModule, FormsModule, RouterModule, ConfirmDialog],
    providers: [CustomerSupportService, MessageService, ConfirmationService]
})
export class CustomerSupportModule {}
