import { createFeatureSelector } from '@ngrx/store';
import { NotificationsState } from './notifications.state';

export const selectNotificationState = createFeatureSelector<NotificationsState>('notification');
