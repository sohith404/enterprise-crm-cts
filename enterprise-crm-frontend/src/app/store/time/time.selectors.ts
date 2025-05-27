import { createFeatureSelector } from '@ngrx/store';
import { TimeState } from './time.reducer';

export const selectTimeState = createFeatureSelector<TimeState>('time');
