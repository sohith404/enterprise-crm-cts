import { createReducer, on } from '@ngrx/store';
import { updateTime } from './time.action';

export interface TimeState {
    currentTime: Date;
}

export const initialState: TimeState = {
    currentTime: new Date()
};

export const timeReducer = createReducer(
    initialState,
    on(updateTime, (state, { time }) => ({
        ...state,
        currentTime: time
    }))
);
