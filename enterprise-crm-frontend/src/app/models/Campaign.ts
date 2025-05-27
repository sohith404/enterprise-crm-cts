import { NotificationType } from './NotificationType';

export class Campaign {
    campaignID?: number;
    name!: string;
    startDate!: Date;
    endDate!: Date;
    type!: NotificationType;
    customerInteractions?: number;
    trackingUrl?: string;
    status?: string;
}
