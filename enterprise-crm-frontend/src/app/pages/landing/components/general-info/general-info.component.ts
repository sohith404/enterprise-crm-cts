import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectCurrentUser } from '../../../../store/index.selectors';
import { Employee } from '../../../../models/Employee';
import { selectNotificationState } from '../../../../store/notifications/notifications.selector';
import { Notification } from '../../../../models/Notification';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-general-info',
    standalone: false,
    templateUrl: './general-info.component.html',
    styleUrl: './general-info.component.scss'
})
export class GeneralInfoComponent implements OnInit, OnDestroy {
    user!: Employee | null;
    notificationList: Notification[] | null = null; // Initialize as null
    private notificationSubscription!: Subscription;

    constructor(private store: Store) {}

    ngOnInit(): void {
        this.store.select(selectCurrentUser).subscribe((user) => {
            this.user = user;
        });

        this.notificationSubscription = this.store.select(selectNotificationState).subscribe((notifications) => {
            this.notificationList = notifications?.notification ? [...notifications.notification].reverse().slice(0, 3) : [];
        });
    }

    ngOnDestroy(): void {
        if (this.notificationSubscription) {
            this.notificationSubscription.unsubscribe();
        }
    }
}
