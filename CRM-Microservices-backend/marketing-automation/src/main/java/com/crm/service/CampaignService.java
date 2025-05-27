package com.crm.service;
import com.crm.dto.CampaignDTO; 
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import java.util.List;
import java.util.Map;
public interface CampaignService {
    public List<CampaignDTO> retrieveAllCampaigns();
    public CampaignDTO getCampaignById(Long campaignId);
    public CampaignDTO createCampaign(CampaignDTO campaignDTO);
    public boolean saveCampaigns(CampaignDTO campaignDTO);
    public CampaignDTO updateCampaign(Long campaignId,CampaignDTO campaignDTO);
    public boolean deleteCampaign(Long campaignId);
    List<CampaignDTO> getByType(Type type);
	List<CampaignDTO> createCampaigns(List<CampaignDTO> campaignDTOs) throws CampaignNotFoundException;
	public String trackCampaignClick(Long campaignId) throws CampaignNotFoundException;
	public Map<Type, Map<String, Object>> getCampaignReachAnalysisByType();
} 
