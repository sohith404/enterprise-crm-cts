import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';

import { Toast } from 'primeng/toast';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { LayoutModule } from './modules/layout/layout.module';
import { AuthModule } from './modules/auth/auth.module';
import { StoreModule } from '@ngrx/store';
import { authReducer } from './store/auth/auth.reducer';
import { timeReducer } from './store/time/time.reducer';
import { LandingModule } from './modules/pages/landing/landing.module';
import { AdminModule } from './modules/admin/admin.module';
import { EffectsModule } from '@ngrx/effects';
import { AuthEffects } from './store/auth/auth.effect';
import { SalesAutomationModule } from './modules/crm-services/sales-automation/sales-automation.module';
import { CustomerDataManagementModule } from './modules/crm-services/customer-data-management/customer-data-management.module';
import { MarketingAutomationModule } from './modules/crm-services/marketing-automation/marketing-automation.module';
import { CustomerSupportModule } from './modules/crm-services/customer-support/customer-support.module';
import { AnalyticsAndReportingModule } from './modules/crm-services/analytics-and-reporting/analytics-and-reporting.module';
import { MessageService } from 'primeng/api';
import { notificationReducer } from './store/notifications/notifications.reducer';
// import { CustomerDeleteConfirmationComponent } from './pages/crm-services/customer-data-management/customer-delete-confirmation/customer-delete-confirmation.component';

@NgModule({
    declarations: [AppComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        LayoutModule,
        AuthModule,
        LandingModule,
        AdminModule,
        SalesAutomationModule,
        CustomerDataManagementModule,
        MarketingAutomationModule,
        CustomerSupportModule,
        AnalyticsAndReportingModule,
        StoreModule.forRoot({ time: timeReducer, auth: authReducer, notification: notificationReducer }),
        EffectsModule.forRoot([AuthEffects]),
        Toast
    ],
    providers: [
        provideHttpClient(withFetch()),
        provideAnimationsAsync(),
        providePrimeNG({
            theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } }
        })
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
