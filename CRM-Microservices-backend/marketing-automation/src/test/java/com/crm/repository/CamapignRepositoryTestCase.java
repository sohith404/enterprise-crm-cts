package com.crm.repository;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
@DataJpaTest
@ActiveProfiles("test")
class CamapignRepositoryTestCase {
    private final CampaignRepository campaignRepository;
    private final TestEntityManager testEntityManager;
    @Autowired
    public CamapignRepositoryTestCase(CampaignRepository campaignRepository, TestEntityManager testEntityManager) {
        this.campaignRepository = campaignRepository;
        this.testEntityManager = testEntityManager;
    }
	@Test
	void testFindAll_positive() {
		Campaign campaign=new Campaign();
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		testEntityManager.persist(campaign);
		Iterable<Campaign> actual=campaignRepository.findAll();
		assertTrue(actual.iterator().hasNext());
		
	}
	@Test
	void testFindAll_negative() {
		Iterable<Campaign> actual=campaignRepository.findAll();
		assertFalse(actual.iterator().hasNext());
	}
	@Test
	void testFindById_positive() {
		Campaign campaign=new Campaign();
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		Campaign savedCampaign=testEntityManager.persist(campaign);
		Optional<Campaign> optionalCampaign=campaignRepository.findById(savedCampaign.getCampaignID());
		Campaign foundCampaign=optionalCampaign.get();
		assertEquals(savedCampaign.getCampaignID(),foundCampaign.getCampaignID());
		assertEquals(LocalDate.of(2023,06,01),foundCampaign.getStartDate());
		assertEquals(LocalDate.of(2023, 06, 30),foundCampaign.getEndDate());
	}
	@Test
	void testFindById_negative() {
		Optional<Campaign> optionalCampaign=campaignRepository.findById(999L);
		assertFalse(optionalCampaign.isPresent());
	}
	@Test 
	void testSave_positive() {
		Campaign campaign=new Campaign();
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		Campaign savedCampaign=testEntityManager.persist(campaign);
		assertNotNull(savedCampaign.getCampaignID());
		assertEquals("Summer Sale",savedCampaign.getName());
		assertEquals(LocalDate.of(2023,06,01),savedCampaign.getStartDate());
		assertEquals(LocalDate.of(2023, 06, 30),savedCampaign.getEndDate());	
	}
	@Test
	void testSave_negative() {
		try { 
			Campaign campaign=null;
			campaignRepository.save(campaign);
			assertTrue(false);
			
		}catch(Exception e) {
			assertTrue(true);
		}
	}
	@Test
    void testUpdate_positive() {
        Campaign campaign = new Campaign();
        campaign.setName("Black Friday Deals");
        campaign.setStartDate(LocalDate.of(2025, 11, 20));
        campaign.setEndDate(LocalDate.of(2025, 11, 30));
        campaign.setType(Type.EMAIL);
        campaign.setCustomerInteractions(1500);
        Campaign savedCampaign = testEntityManager.persist(campaign);
        
        savedCampaign.setCustomerInteractions(1300);
        savedCampaign.setEndDate(LocalDate.of(2025, 12, 5)); 
        campaignRepository.save(savedCampaign);
        testEntityManager.flush();
        testEntityManager.clear();
        Optional<Campaign> optionalCampaign = campaignRepository.findById(savedCampaign.getCampaignID());
        assertTrue(optionalCampaign.isPresent());
        assertEquals(1300, optionalCampaign.get().getCustomerInteractions());
        assertEquals(LocalDate.of(2025, 12, 5), optionalCampaign.get().getEndDate());
    }
	@Test
	void testDelete_positive() {
		Campaign campaign=new Campaign();
		campaign.setName("Summer Sale");
		campaign.setStartDate(LocalDate.of(2023, 06, 01));
		campaign.setEndDate(LocalDate.of(2023, 06, 30));
		campaign.setType(Type.EMAIL);
		campaign.setCustomerInteractions(1500);
		Campaign savedCampaign=testEntityManager.persist(campaign);
		campaignRepository.deleteById(savedCampaign.getCampaignID());
		Optional<Campaign> optionalCampaign=campaignRepository.findById(savedCampaign.getCampaignID());
		assertFalse(optionalCampaign.isPresent());
	}
	 @Test
	    void testDelete_negative() {
	        try {
	            campaignRepository.deleteById(null);
	            assertTrue(false);
	        } catch (Exception e) {
	            assertTrue(true);
	        }
	    }
	 @Test
	 void testType_positive() {
		Campaign emailCampaign=new Campaign();
		emailCampaign.setName("Summer Sale");
		emailCampaign.setStartDate(LocalDate.of(2023, 06, 01));
		emailCampaign.setEndDate(LocalDate.of(2023, 06, 30));
		emailCampaign.setType(Type.EMAIL);
		emailCampaign.setCustomerInteractions(1500);
		testEntityManager.persist(emailCampaign);
		Campaign smsCampaign=new Campaign();
		smsCampaign.setName("Black Friday");
		smsCampaign.setStartDate(LocalDate.of(2023,11,24));
		smsCampaign.setEndDate(LocalDate.of(2023,11,24));
		smsCampaign.setType(Type.SMS);
		smsCampaign.setCustomerInteractions(1200);
		testEntityManager.persist(smsCampaign);
		
		List<Campaign> savedEmailCampaigns=campaignRepository.findByType(Type.EMAIL);
		assertFalse(savedEmailCampaigns.isEmpty());
		assertEquals(1,savedEmailCampaigns.size());
		
		List<Campaign> savedSMSCampaign=campaignRepository.findByType(Type.SMS);
		assertFalse(savedSMSCampaign.isEmpty());
		assertEquals(1,savedSMSCampaign.size());
	 }
	 @Test
	 void testType_negative() {
		 List<Campaign> savedEmailCampaigns=campaignRepository.findByType(Type.EMAIL);
			assertTrue(savedEmailCampaigns.isEmpty());
			assertEquals(0,savedEmailCampaigns.size());
			
			List<Campaign> savedSMSCampaign=campaignRepository.findByType(Type.SMS);
			assertTrue(savedSMSCampaign.isEmpty());
			assertEquals(0,savedSMSCampaign.size());
	 }
}
