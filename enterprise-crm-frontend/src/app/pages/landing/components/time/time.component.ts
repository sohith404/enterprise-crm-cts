import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { interval, Observable, Subscription } from 'rxjs';
import { updateTime } from '../../../../store/time/time.action';
import { selectCurrentTime } from '../../../../store/index.selectors';

@Component({
    selector: 'app-time',
    templateUrl: './time.component.html',
    styleUrls: ['./time.component.scss'],
    standalone: false
})
export class TimeComponent {
    time$: Observable<any>;
    private sub: Subscription | null = null;
    constructor(private store: Store) {
        this.time$ = this.store.select(selectCurrentTime);
    }
    ngOnInit() {
        this.dispatchTime(); // first run immediately
        this.sub = interval(60000).subscribe(() => this.dispatchTime());
    }
    dispatchTime() {
        const now = new Date();
        this.store.dispatch(updateTime({ time: now }));
    }
    ngOnDestroy() {
        this.sub?.unsubscribe();
    }
}
