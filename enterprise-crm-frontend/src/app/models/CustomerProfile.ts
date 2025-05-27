export type CustomerProfile = {
    customerID: number | null;
    name: string;
    emailId: string;
    phoneNumber: string;
    purchaseHistory: string[];
    segmentationData: SegmentationData;
};

export interface SegmentationData {
    Interest: string;
    Region: string;
    'Purchasing Habits': string;
}
