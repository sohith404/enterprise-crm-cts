import { createAction, props } from '@ngrx/store';

export const updateTime = createAction('[Time] Update Time', props<{ time: Date }>());
