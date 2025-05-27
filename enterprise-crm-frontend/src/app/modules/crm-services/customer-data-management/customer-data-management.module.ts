import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// import { CustomerDataManagementComponent } from '../../../pages/crm-services/customer-data-management/customer-data-management.component';
import { CustomersService } from '../../../pages/crm-services/customer-data-management/service/customers.service';
import { CustomerManagementHeaderComponent } from '../../../pages/crm-services/customer-data-management/customer-management-header/customer-management-header.component';
import { CustomerListComponent } from '../../../pages/crm-services/customer-data-management/customer-list/customer-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterCustomerComponent } from '../../../pages/crm-services/customer-data-management/register-customer/register-customer.component';
import { RouterModule } from '@angular/router';
import { DeleteCustomerComponent } from '../../../pages/crm-services/customer-data-management/delete-customer/delete-customer.component';
import { SearchCustomerComponent } from '../../../pages/crm-services/customer-data-management/search-customer/search-customer.component';
import { CustomerDataManagementComponent } from '../../../pages/crm-services/customer-data-management/customer-data-management.component';
import { UpdateCustomerComponent } from '../../../pages/crm-services/customer-data-management/update-customer/update-customer.component';
import { FilterCustomerComponent } from '../../../pages/crm-services/customer-data-management/filter-customer/filter-customer.component';
// import { CustomerDeleteConfirmationComponent } from '../../../pages/crm-services/customer-data-management/customer-delete-confirmation/customer-delete-confirmation.component';

@NgModule({
    declarations: [CustomerDataManagementComponent, CustomerManagementHeaderComponent, CustomerListComponent, RegisterCustomerComponent, DeleteCustomerComponent, SearchCustomerComponent, UpdateCustomerComponent, FilterCustomerComponent],
    imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
    providers: [CustomersService]
})
export class CustomerDataManagementModule {}
