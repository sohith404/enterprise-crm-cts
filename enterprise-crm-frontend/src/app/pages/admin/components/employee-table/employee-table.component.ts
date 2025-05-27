import { Component, OnInit, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { Employee } from '../../../../models/Employee';
import { AuthService } from '../../../auth/service/auth.service';
import { Store } from '@ngrx/store';
import { selectAuthState } from '../../../../store/auth/auth.selector';
import { Router } from '@angular/router';

@Component({
    selector: 'app-employee-table',
    standalone: false,
    templateUrl: './employee-table.component.html',
    styleUrl: './employee-table.component.scss'
})
export class EmployeeTableComponent implements OnInit {
    @ViewChild('dt') dt!: Table;

    employees!: Employee[];
    selectedEmployees!: Employee[];
    loading: boolean = true;
    searchValue: string = '';
    items: MenuItem[] | undefined;

    constructor(
        private readonly authService: AuthService,
        private readonly confirmationService: ConfirmationService,
        private readonly store : Store,
        private router: Router
    ) {}

    visible: boolean = false;

    showDialog() {
        this.visible = true;
    }
    ngOnInit() {
        this.loading = true;
        this.authService.getAllUser().subscribe((users) => {
            this.employees = users;
            this.loading = false;
        });

        this.store.select(selectAuthState).subscribe((authState) => {
            if(authState.user?.role !== 'MANAGER'){
                this.router.navigate(['/'])
            }
        })

        this.items = [
            {
                label: 'Options',
                items: [
                    {
                        label: 'Update',
                        icon: 'pi pi-pencil',
                        command: () => {
                            this.showDialog();
                        }
                    },
                    {
                        label: 'Delete',
                        icon: 'pi pi-trash',
                        iconStyle: { color: 'red' }
                    }
                ]
            }
        ];
    }

    confirmDelete(event: Event, id: number) {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Do you want to delete this record?',
            header: 'Danger Zone',
            icon: 'pi pi-info-circle',
            rejectLabel: 'Cancel',
            rejectButtonProps: {
                label: 'Cancel',
                severity: 'secondary',
                outlined: true
            },
            acceptButtonProps: {
                label: 'Delete',
                severity: 'danger'
            },

            accept: () => {
                this.authService.deleteUser(id).subscribe((obj) => console.log(obj.status));
                this.employees = this.employees.filter((emp) => emp.id !== id);
            },
            reject: () => {}
        });
    }

    clear() {
        if (this.dt) {
            this.dt.clear();
            this.searchValue = '';
        }
    }
}
