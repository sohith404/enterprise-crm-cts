import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment.development';
import { Campaign } from '../../../../models/Campaign';
export interface CampaignReachByType {
    [key: string]: {
        // Key will be the campaign Type (e.g., 'EMAIL', 'SMS')
        averageInteractions: number;
        highestReachCampaignName?: string;
        highestReachInteractions?: number;
        lowestReachCampaignName?: string;
        lowestReachInteractions?: number;
    };
}
@Injectable({
    providedIn: 'root'
})
export class MarketingAutomationService {
    apiUrl = environment.apiUrl;
    private registerCampaignUrl = this.apiUrl;
    public retrieveCampaignByIdUrl = this.apiUrl;
    public updateCampaignUrl = this.apiUrl;
    public reachAnalysisUrl = this.apiUrl;
    campaignId: any;
    constructor(private http: HttpClient) {}
    public registerCampaign(campaign: Campaign) {
        return this.http.post<Campaign>(this.registerCampaignUrl + '/marketing', campaign);
    }
    public getCampaigns() {
        return this.http.get<Campaign[]>(this.registerCampaignUrl + '/marketing');
    }
    public getCampaignById(id: number) {
        return this.http.get<Campaign>(this.registerCampaignUrl + '/marketing/' + id);
    }
    public deleteCampaign(campaign: Campaign) {
        return this.http.delete<Campaign>(this.registerCampaignUrl + '/marketing/' + campaign.campaignID);
    }
    public updateCampaign(campaign: Campaign) {
        return this.http.put<Campaign>(this.updateCampaignUrl + '/marketing/' + campaign.campaignID, campaign);
    }
    public reachAnalysis(campaign: CampaignReachByType) {
        return this.http.get<CampaignReachByType>(this.reachAnalysisUrl + '/marketing/reach-analysis-by-type');
    }
    public getCampaignId() {
        return this.campaignId;
    }
}
