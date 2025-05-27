import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SalesAutomationComponent } from '../../../pages/crm-services/sales-automation/sales-automation.component';
import { SalesService } from '../../../pages/crm-services/sales-automation/service/sales.service';
import { LayoutModule } from '../../layout/layout.module';
import { HeadingComponent } from '../../../pages/crm-services/sales-automation/components/heading/heading.component';
import { StatsComponent } from '../../../pages/crm-services/sales-automation/components/stats/stats.component';
import { MonthlySalesTrendComponent } from '../../../pages/crm-services/sales-automation/components/monthly-sales-trend/monthly-sales-trend.component';
import { ChartModule } from 'primeng/chart';
import { FluidModule } from 'primeng/fluid';
import { SalesPipelineComponent } from '../../../pages/crm-services/sales-automation/components/sales-pipeline/sales-pipeline.component';
import { MenuModule } from 'primeng/menu';
import { PanelModule } from 'primeng/panel';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { PaginatorModule } from 'primeng/paginator';
import { Dialog } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IftaLabelModule } from 'primeng/iftalabel';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { Tooltip } from 'primeng/tooltip';
import { SpeedDial } from 'primeng/speeddial';
import { InputNumber } from 'primeng/inputnumber';
import { Select } from 'primeng/select';
import { Toast } from 'primeng/toast';
import { CurrencyPipe } from '@angular/common';
import { Ripple } from 'primeng/ripple';
@NgModule({
    declarations: [SalesAutomationComponent, HeadingComponent, StatsComponent, MonthlySalesTrendComponent, SalesPipelineComponent],
    imports: [
        CommonModule,
        LayoutModule,
        ButtonModule,
        ChartModule,
        FluidModule,
        DragDropModule,
        FormsModule,
        PanelModule,
        MenuModule,
        PaginatorModule,
        Dialog,
        InputTextModule,
        ReactiveFormsModule,
        DatePickerModule,
        IftaLabelModule,
        FloatLabel,
        Tooltip,
        SpeedDial,
        InputNumber,
        Select,
        Toast,
        Ripple
    ],
    providers: [SalesService, CurrencyPipe],
    exports: [MonthlySalesTrendComponent]
})
export class SalesAutomationModule {}
