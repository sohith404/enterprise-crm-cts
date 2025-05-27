package com.crm.controller;

import com.crm.dto.CampaignDTO;
import com.crm.enums.Type;
import com.crm.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing marketing campaigns.
 * This controller provides endpoints for creating, retrieving, updating, and deleting campaigns,
 * as well as tracking campaign clicks.
 */
@RestController
@RequestMapping("api/marketing")
@Tag(name = "Campaign Management", description = "Operations related to marketing campaigns")
public class CampaignControllerImpl implements CampaignController {

    private final CampaignService service;

    /**
     * Constructs a new CampaignControllerImpl with the specified CampaignService.
     *
     * @param service The CampaignService to use for campaign operations.
     */
    public CampaignControllerImpl(CampaignService service) {
        this.service = service;
    }

    /**
     * Retrieves a list of all marketing campaigns.
     *
     * @return ResponseEntity containing a list of CampaignDTOs.
     */
    @Override
    @GetMapping
    @Operation(summary = "Get all campaigns", description = "Retrieves a list of all marketing campaigns.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "404", description = "Campaigns not found")
    })
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaignDTO = service.retrieveAllCampaigns();
        return new ResponseEntity<>(campaignDTO, HttpStatus.OK);
    }

    /**
     * Creates a new marketing campaign.
     *
     * @param campaignDTO The CampaignDTO containing the details of the campaign to create.
     * @return ResponseEntity containing the created CampaignDTO.
     */
    @Override
    @PostMapping
    @Operation(summary = "Create a new campaign", description = "Creates a new marketing campaign.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Campaign created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "406", description = "Campaign creation failed")
    })
    public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
        CampaignDTO createdCampaign = service.createCampaign(campaignDTO);
        return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
    }

    /**
     * Retrieves a marketing campaign by its ID.
     *
     * @param campaignId The ID of the campaign to retrieve.
     * @return ResponseEntity containing the CampaignDTO.
     */
    @Override
    @GetMapping("/{campaignId}")
    @Operation(summary = "Get campaign by ID", description = "Retrieves a marketing campaign by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campaign found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable("campaignId") Long campaignId) {
        CampaignDTO campaign = service.getCampaignById(campaignId);
        return new ResponseEntity<>(campaign, HttpStatus.OK);
    }

    /**
     * Updates an existing marketing campaign by its ID.
     *
     * @param campaignId  The ID of the campaign to update.
     * @param campaignDTO The CampaignDTO containing the updated details.
     * @return ResponseEntity containing the updated CampaignDTO.
     */
    @Override
    @PutMapping("/{campaignId}")
    @Operation(summary = "Update campaign by ID", description = "Updates an existing marketing campaign by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campaign updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "304", description = "Campaign not modified")
    })
    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable("campaignId") Long campaignId,
                                                       @Valid @RequestBody CampaignDTO campaignDTO) {
        CampaignDTO campaign = service.updateCampaign(campaignId, campaignDTO);
        return new ResponseEntity<>(campaign, HttpStatus.OK);
    }

    /**
     * Deletes a marketing campaign by its ID.
     *
     * @param campaignId The ID of the campaign to delete.
     * @return ResponseEntity indicating the deletion status.
     */
    @Override
    @DeleteMapping("/{campaignId}")
    @Operation(summary = "Delete campaign by ID", description = "Deletes a marketing campaign by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campaign deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    public ResponseEntity<CampaignDTO> deleteCampaign(@PathVariable("campaignId") Long campaignId) {
        boolean isDeleted = service.deleteCampaign(campaignId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK); // Changed from ACCEPTED to OK
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates multiple marketing campaigns.
     *
     * @param campaignDTOs A list of CampaignDTOs containing the details of the campaigns to create.
     * @return ResponseEntity containing a list of created CampaignDTOs.
     */
    @Override
    @PostMapping("multipleCampaigns")
    @Operation(summary = "Create multiple campaigns", description = "Creates multiple marketing campaigns.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Campaigns created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CampaignDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<List<CampaignDTO>> createCampaigns(@RequestBody List<CampaignDTO> campaignDTOs) {
        List<CampaignDTO> createdCampaigns = service.createCampaigns(campaignDTOs);
        return new ResponseEntity<>(createdCampaigns, HttpStatus.CREATED);
    }

    /**
     * Tracks a click on a marketing campaign link and redirects to a success page.
     *
     * @param campaignId The ID of the campaign.
     * @return ResponseEntity indicating the redirect status.
     */
    @Override
    @GetMapping("/{campaignId}/track")
    @Operation(summary = "Track campaign click", description = "Tracks a click on a marketing campaign link and redirects to a success page.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to success page"),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    public ResponseEntity<Void> trackCampaignClick(@PathVariable Long campaignId) {
        service.trackCampaignClick(campaignId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/success.html"))
                .build();
    }

    /**
     * Retrieves campaign reach analysis grouped by campaign type.
     *
     * This endpoint fetches campaign data and analyzes it to provide insights into
     * campaign performance based on different campaign types (e.g., EMAIL, SMS).
     * The analysis includes average customer interactions, highest and lowest reach campaigns,
     * and their respective interaction counts.
     *
     * @return ResponseEntity containing a map of campaign type to analysis results.
     * - HTTP status 200 (OK) if the analysis is successful, with the analysis results in the response body.
     * - HTTP status 500 (INTERNAL_SERVER_ERROR) if an exception occurs during the analysis,
     * with a null response body
     */
    @Override
    public ResponseEntity<Map<Type, Map<String, Object>>> getReachAnalysisByType() {
        Map<Type, Map<String, Object>> analysisResults = service.getCampaignReachAnalysisByType();
        return ResponseEntity.ok(analysisResults);
    }
}