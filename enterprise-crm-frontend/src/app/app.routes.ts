import { Routes } from '@angular/router';
import { AuthGuard } from './core/gaurds/auth/auth.gaurd';
import { AdminComponent } from './pages/admin/admin.component';
import { EmployeeComponent } from './pages/admin/employee/employee.component';
import { UpdateEmployeeComponent } from './pages/admin/update-employee/update-employee.component';
import { AccessComponent } from './pages/auth/access/access.component';
import { ErrorComponent } from './pages/auth/error/error.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { LandingComponent } from './pages/landing/landing.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { SalesAutomationComponent } from './pages/crm-services/sales-automation/sales-automation.component';
import { CustomerDataManagementComponent } from './pages/crm-services/customer-data-management/customer-data-management.component';
import { CustomerSupportComponent } from './pages/crm-services/customer-support/customer-support.component';
import { MarketingAutomationComponent } from './pages/crm-services/marketing-automation/marketing-automation.component';
import { AnalyticsAndReportingComponent } from './pages/crm-services/analytics-and-reporting/analytics-and-reporting.component';
import { RegisterCustomerComponent } from './pages/crm-services/customer-data-management/register-customer/register-customer.component';
import { UpdateCustomerComponent } from './pages/crm-services/customer-data-management/update-customer/update-customer.component';
import { CreateCampaignComponent } from './pages/crm-services/marketing-automation/components/create-campaign/create-campaign.component';
import { ListCampaignComponent } from './pages/crm-services/marketing-automation/components/list-campaign/list-campaign.component';
import { GetCampaignByIdComponent } from './pages/crm-services/marketing-automation/components/get-campaign-by-id/get-campaign-by-id.component';
import { DeleteCampaignComponent } from './pages/crm-services/marketing-automation/components/delete-campaign/delete-campaign.component';
import { ReachAnalysisComponent } from './pages/crm-services/marketing-automation/components/reach-analysis/reach-analysis.component';
import { CreateTicketComponent } from './pages/crm-services/customer-support/components/create-ticket/create-ticket.component';
import { UpdateTicketComponent } from './pages/crm-services/customer-support/components/update-ticket/update-ticket.component';
import { DeleteTicketComponent } from './pages/crm-services/customer-support/components/delete-ticket/delete-ticket.component';
import { SalesAnalyticsComponent } from './pages/crm-services/analytics-and-reporting/sales-analytics/sales-analytics.component';
import { MarketingAnalyticsComponent } from './pages/crm-services/analytics-and-reporting/marketing-analytics/marketing-analytics.component';
import { CustomersAnalyticsComponent } from './pages/crm-services/analytics-and-reporting/customers-analytics/customers-analytics.component';
import { SupportAnalyticsComponent } from './pages/crm-services/analytics-and-reporting/support-analytics/support-analytics.component';
import { ProfileComponent } from './pages/profile/profile.component';

export const appRoutes: Routes = [
    { path: '', component: LandingComponent, canActivate: [AuthGuard] },
    {
        path: 'pages',
        children: [
            { path: '', redirectTo: 'landing', pathMatch: 'full' },
            { path: 'profile', component: ProfileComponent },
            { path: 'landing', component: LandingComponent },
            {
                path: 'admin',
                children: [
                    { path: '', component: AdminComponent },
                    { path: 'employee/add', component: EmployeeComponent },
                    { path: 'employee/:id', component: UpdateEmployeeComponent }
                ],
                canActivate: [AuthGuard]
            },
            {
                path: 'services',
                children: [
                    { path: '', redirectTo: 'customer-data-management', pathMatch: 'full' },
                    {
                        path: 'customer-data-management',
                        children: [
                            { path: '', component: CustomerDataManagementComponent },
                            { path: 'add-customer', component: RegisterCustomerComponent },
                            // {path: 'update-customer', component:UpdateCustomerComponent},
                            { path: 'update-customer/:id', component: UpdateCustomerComponent }
                        ]
                    },
                    { path: 'sales-automation', component: SalesAutomationComponent },
                    {
                        path: 'customer-support',
                        children: [
                            { path: '', component: CustomerSupportComponent },
                            { path: 'add-ticket', component: CreateTicketComponent },
                            { path: 'update-ticket/:id', component: UpdateTicketComponent },
                            { path: 'delete-ticket', component: DeleteTicketComponent }
                        ]
                    },
                    {
                        path: 'marketing-automation',
                        children: [
                            { path: '', component: MarketingAutomationComponent },
                            { path: 'createCampaign', component: CreateCampaignComponent },
                            { path: 'listCampaign', component: ListCampaignComponent },
                            { path: 'getCampaignById/:id', component: GetCampaignByIdComponent },
                            { path: 'deleteCampaign', component: DeleteCampaignComponent },
                            { path: 'reachAnalysis', component: ReachAnalysisComponent } // Assuming you want to use the same component for viewing by ID
                        ]
                    },
                    {
                        path: 'analytics-and-reporting',
                        children: [
                            { path: '', component: AnalyticsAndReportingComponent },
                            { path: 'sales-reports', component: SalesAnalyticsComponent },
                            { path: 'customer-reports', component: CustomersAnalyticsComponent },
                            { path: 'marketing-reports', component: MarketingAnalyticsComponent },
                            { path: 'support-reports', component: SupportAnalyticsComponent }
                        ]
                    }
                ],
                canActivate: [AuthGuard]
            }
        ]
    },
    {
        path: 'auth',
        children: [
            { path: 'login', component: LoginComponent },
            { path: 'error', component: ErrorComponent },
            { path: 'access', component: AccessComponent }
        ]
    },
    { path: 'notfound', component: NotFoundComponent },
    { path: '**', redirectTo: '/notfound', pathMatch: 'full' }
];
