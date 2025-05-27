import { Component } from '@angular/core';
import { Store } from '@ngrx/store';

import { Observable } from 'rxjs';
import { selectCurrentTime } from '../../../../store/index.selectors';

@Component({
    selector: 'app-date',
    templateUrl: './date.component.html',
    styleUrls: ['./date.component.scss'],
    standalone: false
})
export class DateComponent {
    time$: Observable<any>;

    constructor(private store: Store) {
        this.time$ = this.store.select(selectCurrentTime);
    }
}
