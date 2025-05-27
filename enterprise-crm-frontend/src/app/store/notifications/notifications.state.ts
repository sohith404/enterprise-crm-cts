import { Notification } from '../../models/Notification';

export interface NotificationsState {
    notification: Notification[] | null;
}

export const initialNotificationState: NotificationsState = {
    notification: []
};
