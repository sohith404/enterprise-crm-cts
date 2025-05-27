package com.crm.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.crm.dto.CampaignDTO;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
import com.crm.mapper.CampaignMapper;
import com.crm.repository.CampaignRepository;
import com.crm.service.CampaignServiceImpl;
@DataJpaTest
@ActiveProfiles("test")
class CampaignControllerTestCase {

    @Mock
    private CampaignServiceImpl campaignServiceImpl;

    @Mock
    private CampaignRepository campaignRepository;

    @MockitoBean
    private CampaignMapper campaignMapper;

    @InjectMocks
    private CampaignControllerImpl campaignControllerImpl;

    @MockitoBean
    private JavaMailSender mailSender;
    @AfterEach
    void tearDown() {
        campaignServiceImpl = null;
        campaignControllerImpl = null;
    }
    
    private CampaignDTO createCampaignDTO() {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setCampaignID(1L);
        campaignDTO.setName("Summer Sale");
        campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
        campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
        campaignDTO.setType(Type.EMAIL);
        campaignDTO.setCustomerInteractions(1500);
        campaignDTO.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
        return campaignDTO;
    }
    private Campaign createCampaign() {
        Campaign campaign = new Campaign();
        campaign.setCampaignID(1L);
        campaign.setName("Summer Sale");
        campaign.setStartDate(LocalDate.of(2023, 06, 01));
        campaign.setEndDate(LocalDate.of(2023, 06, 30));
        campaign.setType(Type.EMAIL);
        campaign.setCustomerInteractions(1500);
        campaign.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
        return campaign;
    } 
    @Test
    void testGetAllCampaigns_positive() {
    	 List<CampaignDTO> campaigns = new ArrayList<>();
         campaigns.add(new CampaignDTO());
         when(campaignServiceImpl.retrieveAllCampaigns()).thenReturn(campaigns);
         ResponseEntity<List<CampaignDTO>> response = campaignControllerImpl.getAllCampaigns();
         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertNotNull(response.getBody());
         assertEquals(1, response.getBody().size());
    }
    @Test
    void testGetCampaignById_positive() throws CampaignNotFoundException {
        Long campaignId = 1L;
        CampaignDTO expectedCampaign = new CampaignDTO();
        expectedCampaign.setCampaignID(campaignId);
        when(campaignServiceImpl.getCampaignById(campaignId)).thenReturn(expectedCampaign);
        ResponseEntity<?> response = campaignControllerImpl.getCampaignById(campaignId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCampaign, response.getBody());
    }
   
    @Test
    void testGetCampaignById_negative() throws CampaignNotFoundException {
        Long campaignId = 1L;
        String errorMessage = null;
        when(campaignServiceImpl.getCampaignById(campaignId)).thenThrow(new CampaignNotFoundException(errorMessage));
        assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.getCampaignById(campaignId));

    }
    @Test
    void testGetAllCampaigns_notFound() throws CampaignNotFoundException {
        when(campaignServiceImpl.retrieveAllCampaigns()).thenThrow(new CampaignNotFoundException("Campaigns not found"));
        assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.getAllCampaigns());
    }
    @Test
    void testCreateCampaign_positive() {
        try {
            CampaignDTO campaignDTO = createCampaignDTO();
            Campaign campaign = createCampaign();
            when(campaignMapper.mapToCampaign(any(CampaignDTO.class))).thenReturn(campaign);
            when(campaignServiceImpl.createCampaign(any(CampaignDTO.class))).thenReturn(campaignDTO);
            ResponseEntity<?> actual = campaignControllerImpl.createCampaign(campaignDTO);
            assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }
    @Test
    void testCreateCampaign_negative() {
        CampaignDTO campaignDTO = new CampaignDTO();
        when(campaignServiceImpl.createCampaign(any(CampaignDTO.class))).thenThrow(new CampaignNotFoundException("Invalid campaign type"));
        assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.createCampaign(campaignDTO));

    }
    @Test
    void testUpdateCampaign_positive() {
        try {
            CampaignDTO campaignDTO = createCampaignDTO();
            when(campaignServiceImpl.updateCampaign(1L, campaignDTO)).thenReturn(campaignDTO);
            ResponseEntity<CampaignDTO> actual = campaignControllerImpl.updateCampaign(1L, campaignDTO);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(campaignDTO, actual.getBody());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }
    @Test
    void testUpdateCampaign_negative() {
        CampaignDTO campaignDTO = createCampaignDTO();
        when(campaignServiceImpl.updateCampaign(1L, campaignDTO)).thenThrow(new CampaignNotFoundException("Campaign Not Found with the ID"));
        assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.updateCampaign(1L, campaignDTO));
    }
    @Test
    void testDeleteCampaign_positive() {
        try {
            when(campaignServiceImpl.deleteCampaign(1L)).thenReturn(true);
            ResponseEntity<CampaignDTO> actual = campaignControllerImpl.deleteCampaign(1L);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
        } catch (CampaignNotFoundException e) {
            assertTrue(false);
        }
    }
    @Test
    void testDeleteCampaign_negative() {
        when(campaignServiceImpl.deleteCampaign(1L)).thenReturn(false);
        ResponseEntity<CampaignDTO> response = campaignControllerImpl.deleteCampaign(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void test_createCampaigns_success() throws CampaignNotFoundException {
        // Arrange
        List<CampaignDTO> inputDTOs = new ArrayList<>();
        CampaignDTO campaignDTO1 = createCampaignDTO();
        CampaignDTO campaignDTO2 = createCampaignDTO();
        inputDTOs.add(campaignDTO1);
        inputDTOs.add(campaignDTO2);
        List<CampaignDTO> createdDTOs = new ArrayList<>(inputDTOs); // Same list for simplicity
        when(campaignServiceImpl.createCampaigns(inputDTOs)).thenReturn(createdDTOs);
        ResponseEntity<List<CampaignDTO>> response = campaignControllerImpl.createCampaigns(inputDTOs);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDTOs, response.getBody());
        verify(campaignServiceImpl).createCampaigns(inputDTOs);
    }
    @Test
    void testCreateCampaigns_campaignNotFoundException() throws CampaignNotFoundException {
        // Arrange
    	List<CampaignDTO> inputDTOs = new ArrayList<>();
        when(campaignServiceImpl.createCampaigns(inputDTOs)).thenThrow(new CampaignNotFoundException("Campaign not found"));
        
        // Act & Assert
        assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.createCampaigns(inputDTOs));
    }
    @Test
    void testCreateCampaigns_nullPointerException() throws CampaignNotFoundException {
    	 List<CampaignDTO> campaignDTOs = new ArrayList<>();
         when(campaignServiceImpl.createCampaigns(campaignDTOs)).thenThrow(new CampaignNotFoundException("Bad request"));
         assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.createCampaigns(campaignDTOs));
    }
    @Test
    void testTrackCampaignClick_success() throws CampaignNotFoundException {
        Long campaignId = 1L;
        when(campaignServiceImpl.trackCampaignClick(campaignId)).thenReturn("http://localhost:3004/success.html");
        ResponseEntity<Void> response = campaignControllerImpl.trackCampaignClick(campaignId);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(URI.create("/success.html"), response.getHeaders().getLocation());
        verify(campaignServiceImpl).trackCampaignClick(campaignId);
    }
    @Test
    void testTrackCampaignClick_campaignNotFound() throws CampaignNotFoundException {
        Long campaignId = 1L;
        doThrow(new CampaignNotFoundException("Campaign not found")).when(campaignServiceImpl).trackCampaignClick(campaignId);
        assertThrows(CampaignNotFoundException.class, () -> campaignControllerImpl.trackCampaignClick(campaignId));
        verify(campaignServiceImpl).trackCampaignClick(campaignId);
    }
    @Test
    void testGetReachAnalysisByType_shouldReturnOkWithAnalysis() {
        Map<Type, Map<String, Object>> mockAnalysis = new HashMap<>();
        Map<String, Object> emailAnalysis = new HashMap<>();
        emailAnalysis.put("averageInteractions", 10.0);
        emailAnalysis.put("highestReachCampaign", "Test Email");
        mockAnalysis.put(Type.EMAIL, emailAnalysis);
        when(campaignServiceImpl.getCampaignReachAnalysisByType()).thenReturn(mockAnalysis);
        ResponseEntity<Map<Type, Map<String, Object>>> response = campaignControllerImpl.getReachAnalysisByType();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAnalysis, response.getBody());
    }
    @Test
    void testGetReachAnalysisByType_shouldReturnOkWithNullAnalysis() {
        when(campaignServiceImpl.getCampaignReachAnalysisByType()).thenReturn(null);
        ResponseEntity<Map<Type, Map<String, Object>>> responseEntity = campaignControllerImpl.getReachAnalysisByType();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }
}