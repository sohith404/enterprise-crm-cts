package com.crm.service;
import static org.junit.jupiter.api.Assertions.*;     
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.crm.dto.EmailFormat;
import com.crm.dto.NotificationDTO;
import com.crm.feign.Proxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.crm.mapper.CampaignMapperImpl;
import com.crm.repository.CampaignRepository;
import com.crm.dto.CampaignDTO;
import com.crm.entities.*;
import com.crm.enums.Type;
import com.crm.exception.CampaignNotFoundException;
@ExtendWith(MockitoExtension.class)
class CampaignServiceTestCase {
	@Mock
	private CampaignRepository campaignRepository;
	@Mock
	private CampaignMapperImpl mapper;
	@Mock
	private Proxy proxy;
	@InjectMocks
	private CampaignServiceImpl campaignServiceImpl;
	private List<Campaign> campaigns;
    private CampaignDTO campaignDTO;
    private Campaign campaign;
	@BeforeEach
	void setup() {
		campaign=new Campaign();
		campaign.setCampaignID(1L);
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		campaign.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
		campaignDTO=new CampaignDTO();
		campaignDTO.setCampaignID(1L);
		campaignDTO.setName("Summer Sale");
		campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
		campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
		campaignDTO.setType(Type.EMAIL);
		campaignDTO.setCustomerInteractions(1500);
		campaignDTO.setTrackingUrl(campaign.getTrackingUrl());
		
	}
   @Test
	void test_getAllCampaigns_positive() throws CampaignNotFoundException{
		campaign=new Campaign();
		when(campaignRepository.findAll()).thenReturn(Arrays.asList(campaign));
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			verify(campaignRepository,times(1)).findAll();
			assertEquals(1,actual.size());
		}catch(CampaignNotFoundException e) {
			assertFalse(true);
		}
	}
	@Test
	void test_getAllCampaigns_emptyList_negative() {
		when(campaignRepository.findAll()).thenReturn(Arrays.asList());
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			assertFalse(actual.size()>0);
		}catch(CampaignNotFoundException e) {
			assertTrue(true);
		}
	}
	@Test
	void test_getAllCampaigns_whenRepositoryReturnsMoreThanEntity_positive() throws CampaignNotFoundException {
		when(campaignRepository.findAll()).thenAnswer(invocation->{
			Campaign campaign1=new Campaign();
			campaign1.setCampaignID(1L);
			campaign1.setName("Summer Sale");
			campaign1.setStartDate(LocalDate.of(2023, 06, 01));
			campaign1.setEndDate(LocalDate.of(2023, 06, 30));
			campaign1.setType(Type.EMAIL);		
			Campaign campaign2=new Campaign();
			campaign2.setCampaignID(1L);
			campaign2.setName("Summer Sale");
			campaign2.setStartDate(LocalDate.of(2023, 06, 01));
			campaign2.setEndDate(LocalDate.of(2023, 06, 30));
			campaign2.setType(Type.EMAIL);
			return Arrays.asList(campaign1,campaign2);
			});
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			verify(campaignRepository,times(1)).findAll();
			assertTrue(actual.size()>1);
		}catch(CampaignNotFoundException e) {
			assertFalse(true);
		}
	}
	@Test
	void test_getAllCampaigns_whenRepositoryReturnsMoreThanEntity_negative() {
		when(campaignRepository.findAll()).thenAnswer(invocation->{
			return Arrays.asList();
		});
		try {
			List<CampaignDTO> actual=campaignServiceImpl.retrieveAllCampaigns();
			assertFalse(actual.size()>0);
		}catch(CampaignNotFoundException e) {
			assertTrue(true);
		}
	}
	@Test
	void test_saveCampaigns_positive() throws CampaignNotFoundException{
		CampaignDTO campaignDTO1=new CampaignDTO();
		campaignDTO1.setCampaignID(1L);
		campaignDTO1.setName("Summer Sale");
		campaignDTO1.setStartDate(LocalDate.of(2023, 06, 01));
		campaignDTO1.setEndDate(LocalDate.of(2023, 06, 30));
		campaignDTO1.setType(Type.EMAIL);
		campaignDTO1.setCustomerInteractions(1500);
		campaign=new Campaign();
		campaign.setCampaignID(1L);
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		when(campaignRepository.save(any())).thenReturn(campaign);
		boolean actual=campaignServiceImpl.saveCampaigns(campaignDTO1);
		assertTrue(actual);	
	}
	@Test
	void test_saveCamapigns_negative() {
		campaignDTO=new CampaignDTO();
		when(campaignRepository.save(any())).thenThrow(new CampaignNotFoundException("Campaign not saved"));
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.saveCampaigns(campaignDTO));
	}
	@Test 
	void test_getCampaignById_positive() throws CampaignNotFoundException{
	when(campaignRepository.findById(1L)).thenAnswer(new Answer<Optional<Campaign>>() {
		@Override
		public Optional<Campaign> answer(InvocationOnMock invocation) throws Throwable {
			Optional<Campaign> optionalCampaign=null;
			Long campaignId=(Long) invocation.getArgument(0);
			if(campaignId==1L) {
				 campaign=new Campaign();
				 campaign.setCampaignID(1L);
				 campaign.setName("Summer Sale");
				 campaign.setStartDate(LocalDate.of(2023, 06, 01));
				 campaign.setEndDate(LocalDate.of(2023, 06, 30));
				 campaign.setType(Type.EMAIL);
				 campaign.setCustomerInteractions(1500);
				 optionalCampaign=Optional.of(campaign);
			}
			return optionalCampaign;
		}
		
	});
	   CampaignDTO expectedDTO=new CampaignDTO();
	   expectedDTO.setCampaignID(1L);
	   expectedDTO.setName("Summer Sale");
	   expectedDTO.setStartDate(LocalDate.of(2023, 06, 01));
	   expectedDTO.setEndDate(LocalDate.of(2023, 06, 30));
	   expectedDTO.setType(Type.EMAIL);
	   expectedDTO.setCustomerInteractions(1500);
	   when(mapper.mapToDTO(any())).thenReturn(expectedDTO);
	   CampaignDTO actual=campaignServiceImpl.getCampaignById(1L);	
	   assertEquals(1L,actual.getCampaignID());	
	}
	@Test
	void test_getCampaignById_negative() {
        when(campaignRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.getCampaignById(1L));
	}
	 @Test
	    void test_updateCampaign_positive() throws CampaignNotFoundException {
	        campaign = new Campaign();
	        campaign.setCampaignID(1L);
	        campaign.setName("Summer Sale");
	        campaign.setStartDate(LocalDate.of(2023, 06, 01));
	        campaign.setEndDate(LocalDate.of(2023, 06, 30));
	        campaign.setType(Type.EMAIL);
	        campaign.setCustomerInteractions(1500);
	        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
	        CampaignDTO updatedCampaignDTO = new CampaignDTO();
	        updatedCampaignDTO.setName("Summer Sale Bonanza");
	        campaignDTO = new CampaignDTO();
	        campaignDTO.setCampaignID(1L);
	        campaignDTO.setName("Summer Sale Bonanza");
	        campaignDTO.setStartDate(LocalDate.of(2023, 06, 01));
	        campaignDTO.setEndDate(LocalDate.of(2023, 06, 30));
	        campaignDTO.setType(Type.EMAIL);
	        campaignDTO.setCustomerInteractions(1500);
	        when(mapper.mapToDTO(any())).thenReturn(campaignDTO);
	        CampaignDTO actual = campaignServiceImpl.updateCampaign(1L, updatedCampaignDTO);
	        assertEquals("Summer Sale Bonanza", actual.getName());
	    }
	 @Test
	 void test_updateCampaigns_negative() {
	     try {
	         when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
	         campaignServiceImpl.updateCampaign(1L, new CampaignDTO());
	         fail("Expected CampaignNotFoundException, but no exception was thrown.");
	     } catch (CampaignNotFoundException e) {
	    	 assertTrue(true);
	     }
	 }
	@Test
	void test_deleteCampaign_positive() throws CampaignNotFoundException{
		when(campaignRepository.findById(1L)).thenAnswer(new Answer<Optional<Campaign>>() {
			@Override
			public Optional<Campaign> answer(InvocationOnMock invocation) throws Throwable{
				Optional<Campaign> optionalCampaign=null;
	        	 Long campaignId=(Long)invocation.getArgument(0);
	        	 if(campaignId==1L) {
	        		 campaign=new Campaign();
	        		 campaign.setCampaignID(1L);
	        		 campaign.setName("Summer Sale");
	        		 campaign.setStartDate(LocalDate.of(2023, 06, 01));
	        		 campaign.setEndDate(LocalDate.of(2023, 06, 30));
	        		 campaign.setType(Type.EMAIL);
	        		 campaign.setCustomerInteractions(1500);
	        		 optionalCampaign=Optional.of(campaign);
	        	 }
	        	 return optionalCampaign;
			}
		});
		try {
			boolean actual=campaignServiceImpl.deleteCampaign(1L);
			verify(campaignRepository,times(1)).deleteById(1L);
			assertTrue(actual);
		}catch(CampaignNotFoundException e) {
			assertTrue(false);
		}
	}
	@Test
	void test_deleteCamapign_negative() {
		when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(CampaignNotFoundException.class,()->campaignServiceImpl.deleteCampaign(1L));
	}
	@Test
    void test_getByType_validType_returnsCampaigns() {
		
		when(campaignRepository.findByType(Type.EMAIL)).thenReturn(Arrays.asList(campaign));
        when(mapper.mapToDTO(campaign)).thenReturn(campaignDTO);       
        List<CampaignDTO> actual = campaignServiceImpl.getByType(Type.EMAIL);
        assertEquals(1, actual.size());
        assertEquals(campaignDTO.getType(),actual.get(0).getType() );
    }
    @Test
    void test_getByType_noCampaignsOfType_returnsEmptyList() {
        when(campaignRepository.findByType(Type.EMAIL)).thenReturn(Arrays.asList());
        List<CampaignDTO> actual = campaignServiceImpl.getByType(Type.EMAIL);
        assertEquals(0, actual.size());
    }
    @Test
    void test_trackCampaignClick_campaignFound_updatesInteractions() throws CampaignNotFoundException {
        Long campaignId = 1L;
        campaign = new Campaign();
        campaign.setCampaignID(campaignId);
        campaign.setCustomerInteractions(10); // Initial interactions

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(any())).thenReturn(campaign); // Mock save

        String result = campaignServiceImpl.trackCampaignClick(campaignId);

        assertEquals("http://localhost:3004/success.html", result);
        assertEquals(11, campaign.getCustomerInteractions()); // Ensure interactions are incremented
        verify(campaignRepository).save(campaign); // Verify save was called
    }

    @Test
    void test_trackCampaignClick_campaignNotFound_throwsException() {
        when(campaignRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(CampaignNotFoundException.class, () -> campaignServiceImpl.trackCampaignClick(1L));
    }
    @Test
    void testCreateCampaign_ValidDates_EmailType() throws CampaignNotFoundException {
        CampaignDTO inputDTO = new CampaignDTO();
        inputDTO.setStartDate(LocalDate.now());
        inputDTO.setEndDate(LocalDate.now().plusDays(7));
        inputDTO.setType(Type.EMAIL);
        inputDTO.setName("Test Campaign");
        inputDTO.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
        campaign = new Campaign();
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setEmailFor("Customer");
		notificationDTO.setStatus("pending");
		notificationDTO.setBody("");
		notificationDTO.setType(Type.EMAIL);
		notificationDTO.setTrackingUrl("");
        campaign.setCampaignID(1L);
        campaign.setStartDate(inputDTO.getStartDate());
        campaign.setEndDate(inputDTO.getEndDate());
        campaign.setType(inputDTO.getType());
        campaign.setName(inputDTO.getName());
        campaign.setTrackingUrl("http://localhost:3004/api/marketing/7/track");
		when(proxy.sendNotification(any(NotificationDTO.class))).thenReturn(notificationDTO);
        when(mapper.mapToCampaign(inputDTO)).thenReturn(campaign);
        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(mapper.mapToDTO(campaign)).thenReturn(inputDTO);
        CampaignDTO resultDTO = campaignServiceImpl.createCampaign(inputDTO);

        assertNotNull(resultDTO.getTrackingUrl());
        assertEquals(Type.EMAIL, resultDTO.getType());
        verify(campaignRepository, times(2)).save(campaign);
    }
    @Test
    void testCreateCampaign_InvalidDates() {
        CampaignDTO inputDTO = new CampaignDTO();
        inputDTO.setStartDate(LocalDate.now().plusDays(7));
        inputDTO.setEndDate(LocalDate.now());

        assertThrows(CampaignNotFoundException.class, () -> campaignServiceImpl.createCampaign(inputDTO));
    }
    @Test
    void test_createCampaigns_multipleCampaigns() throws CampaignNotFoundException {
        List<CampaignDTO> inputDTOs = new ArrayList<>();
		NotificationDTO notificationDTO = new NotificationDTO();
		EmailFormat emailFormat=new EmailFormat();
		emailFormat.setSalutation("Dear Customer,");
		emailFormat.setOpeningLine("Welcome to theCampaign 1I I hope this message finds you well.");
		emailFormat.setBody("Don't miss out on our exclusive Campaign 1 offers !");
		emailFormat.setConclusion("Thanks for being with us,THIS IS AUTO GENERATED MAIL PLEASE DO NOT REPLY THIS MAIL");
		emailFormat.setClosing("Click below link to know more");
		notificationDTO.setType(Type.EMAIL);
		notificationDTO.setStatus("pending");
		notificationDTO.setSubject("This is an Campaign Infomartion with Campaign name"+campaign.getName());
		notificationDTO.setEmailFor("Customer");
		notificationDTO.setTrackingUrl("http://localhost:3004/api/marketing/1/track");
		notificationDTO.setBody(emailFormat.toString());

        CampaignDTO dto1 = new CampaignDTO();
        dto1.setName("Campaign 1");
        dto1.setStartDate(LocalDate.now());
        dto1.setEndDate(LocalDate.now().plusDays(7));
        dto1.setType(Type.EMAIL);
        dto1.setTrackingUrl("http://localhost:3004/api/marketing/1/track");
        dto1.setCustomerInteractions(0);
        CampaignDTO dto2 = new CampaignDTO();
        dto2.setName("Campaign 2");
        dto2.setStartDate(LocalDate.now().plusDays(10));
        dto2.setEndDate(LocalDate.now().plusDays(17));
        dto2.setType(Type.SMS);
        dto2.setTrackingUrl("http://localhost:3004/api/marketing/2/track");
        dto2.setCustomerInteractions(0);
        inputDTOs.add(dto1);
        inputDTOs.add(dto2);
        Campaign campaign1 = new Campaign();
        campaign1.setCampaignID(1L);
        campaign1.setName("Campaign 1");
        campaign1.setStartDate(dto1.getStartDate());
        campaign1.setEndDate(dto1.getEndDate());
        campaign1.setType(dto1.getType());
        campaign1.setTrackingUrl(dto1.getTrackingUrl());
        campaign1.setCustomerInteractions(dto1.getCustomerInteractions());
        Campaign campaign2 = new Campaign();
        campaign2.setCampaignID(2L);
        campaign2.setName("Campaign 2");
        campaign2.setStartDate(dto2.getStartDate());
        campaign2.setEndDate(dto2.getEndDate());
        campaign2.setType(dto2.getType());
        campaign2.setTrackingUrl(dto2.getTrackingUrl());
        campaign2.setCustomerInteractions(dto2.getCustomerInteractions());
        when(mapper.mapToCampaign(dto1)).thenReturn(campaign1);
        when(campaignRepository.save(campaign1)).thenReturn(campaign1);
		when(proxy.sendNotification(any(NotificationDTO.class))).thenReturn(notificationDTO);
        when(mapper.mapToDTO(campaign1)).thenReturn(dto1);
        when(mapper.mapToCampaign(dto2)).thenReturn(campaign2);
        when(campaignRepository.save(campaign2)).thenReturn(campaign2);
        when(mapper.mapToDTO(campaign2)).thenReturn(dto2);
        List<CampaignDTO> createdDTOs = campaignServiceImpl.createCampaigns(inputDTOs);
        assertEquals(2, createdDTOs.size());
        assertEquals("Campaign 1", createdDTOs.get(0).getName());
        assertEquals("Campaign 2", createdDTOs.get(1).getName());
    }


    @Test
    void test_createCampaigns_emptyList() throws CampaignNotFoundException {
        List<CampaignDTO> inputDTOs = new ArrayList<>();
        List<CampaignDTO> createdDTOs = campaignServiceImpl.createCampaigns(inputDTOs);
        assertEquals(0, createdDTOs.size());
    }
    @Test
    void getCampaignReachAnalysisByType_shouldReturnPositiveAnalysis() {
    	campaigns=new ArrayList<>();
    	campaign=new Campaign();
    	Campaign campaign1=new Campaign();
		campaign.setCampaignID(1L);
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		campaign.setTrackingUrl("http://localhost:3004/api/marketing/1/track");
		campaigns.add(campaign);
		campaign1.setCampaignID(2L);
		campaign1.setName("Spring Sale");
		campaign1.setStartDate(LocalDate.of(2025, 06, 01));
		campaign1.setEndDate(LocalDate.of(2025, 06, 20));
		campaign1.setCustomerInteractions(1400);
		campaign1.setType(Type.EMAIL);
		campaign1.setTrackingUrl("http://localhost:3004/api/marketing/2/track");
		campaigns.add(campaign1);
		Campaign campaign2=new Campaign();
		campaign2.setCampaignID(3L);
		campaign2.setName("Diwali Sale");
		campaign2.setStartDate(LocalDate.of(2025, 04, 01));
		campaign2.setEndDate(LocalDate.of(2025, 04, 20));
		campaign2.setCustomerInteractions(1200);
		campaign2.setType(Type.SMS);
		campaign2.setTrackingUrl("http://localhost:3004/api/marketing/3/track");
		campaigns.add(campaign2);
        when(campaignRepository.findAll()).thenReturn(campaigns);
        Map<Type, Map<String, Object>> results = campaignServiceImpl.getCampaignReachAnalysisByType();
        assertNotNull(results);
        Map<String, Object> emailResults = results.get(Type.EMAIL);
        Map<String,Object> smsResults=results.get(Type.SMS);
        assertNotNull(emailResults);
        assertEquals("Summer Sale", emailResults.get("highestReachCampaignName"));
        assertEquals(1400, emailResults.get("lowestReachInteractions"));
        assertEquals("Spring Sale", emailResults.get("lowestReachCampaignName"));
        assertEquals(1500, emailResults.get("highestReachInteractions"));
        assertEquals(1450.0,emailResults.get("averageInteractions"));
        assertEquals("Diwali Sale",smsResults.get("highestReachCampaignName"));
        assertEquals(1200,smsResults.get("lowestReachInteractions"));
        assertEquals(1200,smsResults.get("highestReachInteractions"));
     }
    @Test
    void getCampaignReachAnalysisByType_shouldReturnEmptyMap_whenNoCampaigns() {
        when(campaignRepository.findAll()).thenReturn(new ArrayList<>());
        Map<Type, Map<String, Object>> results = campaignServiceImpl.getCampaignReachAnalysisByType();
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}