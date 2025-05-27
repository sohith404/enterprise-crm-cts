export type SalesOpportunity = {
    opportunityID: number;

    customerID: number;

    estimatedValue: number;

    closingDate: string;

    followUpReminder: string | null;

    salesStage: SalesStage;
};

export enum SalesStage {
    PROSPECTING = 'PROSPECTING',
    QUALIFICATION = 'QUALIFICATION',
    CLOSED_WON = 'CLOSED_WON',
    CLOSED_LOST = 'CLOSED_LOST'
}
