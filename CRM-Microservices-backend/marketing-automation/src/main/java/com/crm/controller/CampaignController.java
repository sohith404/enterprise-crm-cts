package com.crm.controller;
import com.crm.dto.CampaignDTO;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RequestMapping("api/marketing")
public interface CampaignController {
	@GetMapping
	public ResponseEntity<List<CampaignDTO>> getAllCampaigns();
	@PostMapping
	public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO);
	@PostMapping("multipleCampaigns")
	ResponseEntity<List<CampaignDTO>> createCampaigns(@RequestBody List<CampaignDTO> campaignDTOs);
	@GetMapping("/{campaignId}")
	public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable("campaignId") Long campaignId);
	@PutMapping("/{campaignId}")
	public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable("campaignId") Long campaignId,@Valid @RequestBody CampaignDTO campaignDTO);
	@DeleteMapping("/{campaignId}")
	public ResponseEntity<CampaignDTO> deleteCampaign(@PathVariable("campaignId") Long campaignId);
	@GetMapping("/{campaignId}/track")
	public ResponseEntity<Void> trackCampaignClick(@PathVariable("campaignId") Long campaignId) throws CampaignNotFoundException;
    @GetMapping("/reach-analysis-by-type")
    public ResponseEntity<Map<Type, Map<String, Object>>> getReachAnalysisByType();
}
