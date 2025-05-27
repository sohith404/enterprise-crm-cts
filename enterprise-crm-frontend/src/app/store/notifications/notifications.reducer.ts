import { createReducer, on } from '@ngrx/store';
import { initialNotificationState } from './notifications.state';
import { addNotification } from './notiffications.actions'; // Import the Notification interface

export const notificationReducer = createReducer(
    initialNotificationState,
    on(addNotification, (state, { notification }) => ({
        ...state,
        notification: [...(state.notification || []), notification]
    }))
);
