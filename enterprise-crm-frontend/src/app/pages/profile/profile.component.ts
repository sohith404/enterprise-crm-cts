import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { Employee } from '../../models/Employee';
import { selectAuthState } from '../../store/auth/auth.selector';

@Component({
    selector: 'app-profile',
    standalone: false,
    templateUrl: './profile.component.html',
    styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
    user!: Employee;

    constructor(private store: Store) {}

    ngOnInit(): void {
        this.store.select(selectAuthState).subscribe((authState) => (this.user = authState.user!));
    }
}
