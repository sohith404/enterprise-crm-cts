import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Employee } from '../../../models/Employee';
import { select, Store } from '@ngrx/store';
import { selectCurrentUser } from '../../../store/index.selectors';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-menu',
    standalone: false,
    templateUrl: './menu.component.html',
    styleUrl: './menu.component.scss'
})
export class MenuComponent {
    model: MenuItem[] = [];
    user$!: Observable<Employee | null>;

    constructor(private readonly store: Store) {}

    ngOnInit() {
        this.user$ = this.store.pipe(select(selectCurrentUser));
        this.model = [
            {
                label: 'Home',
                items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] }]
            },
            {
                label: 'Modules',
                items: [
                    { label: 'Customers', icon: 'pi pi-fw pi-users', routerLink: ['pages/services/customer-data-management'] },
                    { label: 'Sales', icon: 'pi pi-fw pi-indian-rupee', routerLink: ['pages/services/sales-automation'] },
                    { label: 'Support', icon: 'pi pi-fw pi-phone', routerLink: ['pages/services/customer-support'] },
                    { label: 'Marketing', icon: 'pi pi-fw pi-megaphone', routerLink: ['pages/services/marketing-automation'] },
                    { label: 'Analytics', icon: 'pi pi-fw pi-chart-bar', routerLink: ['pages/services/analytics-and-reporting'] }
                ]
            }
        ];
        this.user$.subscribe((user) => {
            if (user?.role === 'MANAGER') {
                this.model.push({
                    label: 'ADMIN',
                    items: [
                        { label: 'Register Employee', icon: 'pi pi-fw pi-plus', routerLink: ['/pages/admin/employee/add'] },
                        { label: 'View Employees', icon: 'pi pi-fw pi-eye', routerLink: ['/pages/admin'] }
                    ]
                });
            } else {
                this.model = this.model.filter((obj) => obj.label !== 'ADMIN');
            }
        });
    }
}
