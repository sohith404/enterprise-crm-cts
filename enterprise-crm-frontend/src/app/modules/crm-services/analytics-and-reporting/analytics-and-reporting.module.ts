import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsAndReportingComponent } from '../../../pages/crm-services/analytics-and-reporting/analytics-and-reporting.component';
import { AnalyticsService } from '../../../pages/crm-services/analytics-and-reporting/service/analytics.service';
import { HeaderComponent } from '../../../pages/crm-services/analytics-and-reporting/components/header/header.component';
import { AnalyticsStatsComponent } from '../../../pages/crm-services/analytics-and-reporting/components/stats/stats.component';
import { FluidModule } from 'primeng/fluid';
import { SalesAutomationModule } from '../sales-automation/sales-automation.module';
import { CustomermoduleComponent } from '../../../pages/crm-services/analytics-and-reporting/components/customermodule/customermodule.component';
import { ChartModule } from 'primeng/chart';
import { MarketingmoduleComponent } from '../../../pages/crm-services/analytics-and-reporting/components/marketingmodule/marketingmodule.component';
import { SupportmoduleComponent } from '../../../pages/crm-services/analytics-and-reporting/components/supportmodule/supportmodule.component';
import { LandingModule } from '../../pages/landing/landing.module';
import { TabsSalesComponent } from '../../../pages/crm-services/analytics-and-reporting/components/tabs-sales/tabs-sales.component';
import { TabsCustomerComponent } from '../../../pages/crm-services/analytics-and-reporting/components/tabs-customer/tabs-customer.component';
import { TabsSupportComponent } from '../../../pages/crm-services/analytics-and-reporting/components/tabs-support/tabs-support.component';
import { TabsMarketingComponent } from '../../../pages/crm-services/analytics-and-reporting/components/tabs-marketing/tabs-marketing.component';
import { SpeedDial } from 'primeng/speeddial';
import { SalesAnalyticsComponent } from '../../../pages/crm-services/analytics-and-reporting/sales-analytics/sales-analytics.component';
import { CustomersAnalyticsComponent } from '../../../pages/crm-services/analytics-and-reporting/customers-analytics/customers-analytics.component';
import { SupportAnalyticsComponent } from '../../../pages/crm-services/analytics-and-reporting/support-analytics/support-analytics.component';
import { MarketingAnalyticsComponent } from '../../../pages/crm-services/analytics-and-reporting/marketing-analytics/marketing-analytics.component';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { OrganizationChartModule } from 'primeng/organizationchart';

@NgModule({
    declarations: [
        AnalyticsAndReportingComponent,
        HeaderComponent,
        AnalyticsStatsComponent,
        CustomermoduleComponent,
        MarketingmoduleComponent,
        SupportmoduleComponent,
        TabsSalesComponent,
        TabsCustomerComponent,
        TabsSupportComponent,
        TabsMarketingComponent,
        SalesAnalyticsComponent,
        CustomersAnalyticsComponent,
        SupportAnalyticsComponent,
        MarketingAnalyticsComponent
    ],
    imports: [CommonModule, FluidModule, SalesAutomationModule, ChartModule, LandingModule, SpeedDial, TableModule, ButtonModule, OrganizationChartModule],
    providers: [AnalyticsService]
})
export class AnalyticsAndReportingModule {}
