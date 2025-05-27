export type SalesReport = {
    id: number;
    reportType: 'CUSTOMER' | 'MARKETING' | 'SALES' | 'SUPPORT';
    generatedDate: string;
    dataPoints: {
        opportunitiesLost: number;
        opportunitiesInProgress: number;
        totalEstimatedValue: number;
        opportunitiesWon: number;
    };
};

export type CustomerReport = {
    id: number;
    reportType: 'CUSTOMER' | 'MARKETING' | 'SALES' | 'SUPPORT';
    generatedDate: string;
    dataPoints: {
        totalCustomers: number;
        inactiveCustomers: number;
        top5Customers: {
            purchaseCount: number;
            customerId: number;
            name: string;
        }[];
    };
};

export type MarketingReport = {
    id: number;
    reportType: 'CUSTOMER' | 'MARKETING' | 'SALES' | 'SUPPORT';
    generatedDate: string;
    dataPoints: {
        campaignsByType: {
            EMAIL: number;
            SMS: number;
        }[];
        totalCampaigns: number;
        totalCustomerInteractions: number;
        topCampaign: string;
        completedCampaigns: number;
        activeCampaigns: number;
        zeroInteractionCampaigns: number;
    };
};

export type SupportReport = {
    id: number;
    reportType: 'CUSTOMER' | 'MARKETING' | 'SALES' | 'SUPPORT';
    generatedDate: string;
    dataPoints: {
        ticketsPerAgent: Record<string, number>;
        openTickets: 3;
        totalTickets: 5;
        closedTickets: 2;
        top5Customers: Record<string, number>;
    };
};

export type AllReports = (SalesReport | CustomerReport | MarketingReport | SupportReport)[];
