import { Component, ElementRef, OnInit } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { selectAuthState } from '../../../store/auth/auth.selector';
import { AuthState } from '../../../store/auth/auth.state';
import { AuthService } from '../../../pages/auth/service/auth.service';
import { Observable } from 'rxjs';
import { Employee } from '../../../models/Employee';
import { selectCurrentUser } from '../../../store/index.selectors';

@Component({
    selector: 'app-sidebar',
    standalone: false,
    templateUrl: './sidebar.component.html',
    styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
    loading = false;
    constructor(
        public el: ElementRef,
        private readonly authservice: AuthService,
        private readonly store: Store
    ) {}
    user$!: Observable<Employee | null>;
    ngOnInit(): void {
        this.user$ = this.store.pipe(select(selectCurrentUser));
    }

    handleLogout() {
        this.loading = true;
        this.authservice.logout();
        this.loading = false;
    }
}
