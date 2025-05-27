import { createAction, props } from '@ngrx/store';
import { Notification } from '../../models/Notification';

export const addNotification = createAction('[Notification] Add Notification', props<{ notification: Notification }>());
