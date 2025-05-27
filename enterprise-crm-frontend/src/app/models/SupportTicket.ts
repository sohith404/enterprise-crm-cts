export interface SupportTicket {
    ticketID?: string;

    customerID: string;

    priority?: 'high' | 'low';

    created?: Date;

    assignedAgent: string;

    issueDescription: string;

    status: 'OPEN' | 'CLOSED';
}

export function getMockSupportTicket(): SupportTicket {
    return {
        ticketID: 'TICKET12345',
        customerID: '1243',
        priority: 'high',
        created: new Date(),
        assignedAgent: 'Agent01',
        issueDescription: 'Sample issue description for testing.',
        status: 'OPEN'
    };
}
