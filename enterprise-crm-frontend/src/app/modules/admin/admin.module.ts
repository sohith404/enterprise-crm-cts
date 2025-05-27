import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from '../../pages/admin/admin.component';
import { EmployeeTableComponent } from '../../pages/admin/components/employee-table/employee-table.component';
import { LayoutModule } from '../layout/layout.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { InputIcon } from 'primeng/inputicon';
import { IconField } from 'primeng/iconfield';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { Slider } from 'primeng/slider';
import { ProgressBar } from 'primeng/progressbar';
import { Tag } from 'primeng/tag';
import { AvatarModule } from 'primeng/avatar';
import { Dialog } from 'primeng/dialog';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { SelectButton } from 'primeng/selectbutton';
import { RouterModule } from '@angular/router';
import { EmployeeComponent } from '../../pages/admin/employee/employee.component';
import { UpdateEmployeeComponent } from '../../pages/admin/update-employee/update-employee.component';

@NgModule({
    declarations: [AdminComponent, EmployeeTableComponent, EmployeeComponent, UpdateEmployeeComponent],
    imports: [
        CommonModule,
        LayoutModule,
        TableModule,
        Tag,
        ButtonModule,
        IconField,
        InputIcon,
        CommonModule,
        MultiSelectModule,
        InputTextModule,
        DropdownModule,
        Slider,
        ProgressBar,
        PasswordModule,
        ReactiveFormsModule,
        AvatarModule,
        Dialog,
        ConfirmDialog,
        SelectButton,
        RouterModule,
        FormsModule
    ]
})
export class AdminModule {}
