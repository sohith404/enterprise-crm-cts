package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.NotificationDTO;
import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.exception.InvalidSalesDetailsException;
import com.crm.feign.Proxy;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SalesOpportunityServiceImplIntegrationTest {

    @Spy
    private SalesOpportunityRepository repository;

    @Spy
    private SalesOpportunityMapper mapper = SalesOpportunityMapper.MAPPER;

    @Spy
    private Proxy proxy;
    @InjectMocks
    private SalesOpportunityServiceImpl service;


    private SalesOpportunity salesOpportunity;
    private SalesOpportunityRequestDTO requestDTO;
    private SalesOpportunityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        salesOpportunity = new SalesOpportunity();
        salesOpportunity.setOpportunityID(1L);
        salesOpportunity.setCustomerID(1L);
        salesOpportunity.setSalesStage(SalesStage.QUALIFICATION);
        salesOpportunity.setEstimatedValue(BigDecimal.valueOf(1000));
        salesOpportunity.setClosingDate(LocalDate.now().plusDays(30));
        salesOpportunity.setFollowUpReminder(LocalDate.now().plusDays(7));

        requestDTO = new SalesOpportunityRequestDTO();
        requestDTO.setCustomerID(1L);
        requestDTO.setSalesStage(SalesStage.QUALIFICATION);
        requestDTO.setEstimatedValue(BigDecimal.valueOf(1000));
        requestDTO.setClosingDate(LocalDate.now().plusDays(30));
        requestDTO.setFollowUpReminder(LocalDate.now().plusDays(7));

        responseDTO = mapper.mapToResponseDTO(salesOpportunity);
    }

    @Test
    void retrieveAllSalesOpportunities_success() {
        List<SalesOpportunity> opportunityList = new ArrayList<>();
        opportunityList.add(salesOpportunity);
        when(repository.findAll()).thenReturn(opportunityList);

        List<SalesOpportunityResponseDTO> result = service.retrieveAllSalesOpportunities();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void retrieveAllSalesOpportunities_noOpportunitiesFound_throwsNoSuchElementException() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> service.retrieveAllSalesOpportunities());
        verify(repository, times(1)).findAll();
    }



    @Test
    void getOpportunitiesByOpportunity_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(salesOpportunity));
        SalesOpportunityResponseDTO result = service.getOpportunitiesByOpportunity(1L);
        assertEquals(responseDTO, result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getOpportunitiesByOpportunity_opportunityNotFound_throwsNoSuchElementException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByOpportunity(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getOpportunitiesByCustomer_success() {
        List<SalesOpportunity> opportunityList = new ArrayList<>();
        opportunityList.add(salesOpportunity);
        when(repository.findByCustomerID(100L)).thenReturn(opportunityList);
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByCustomer(100L);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(repository, times(1)).findByCustomerID(100L);
    }

    @Test
    void getOpportunitiesByCustomer_customerNotFound_throwsNoSuchElementException() {
        when(repository.findByCustomerID(100L)).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByCustomer(100L));
        verify(repository, times(1)).findByCustomerID(100L);
    }

    @Test
    void getOpportunitiesBySalesStage_success() {
        List<SalesOpportunity> opportunityList = new ArrayList<>();
        opportunityList.add(salesOpportunity);
        when(repository.findBySalesStage(SalesStage.QUALIFICATION)).thenReturn(opportunityList);
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesBySalesStage(SalesStage.QUALIFICATION);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(repository, times(1)).findBySalesStage(SalesStage.QUALIFICATION);
    }

    @Test
    void getOpportunitiesBySalesStage_salesStageNotFound_throwsNoSuchElementException() {
        when(repository.findBySalesStage(SalesStage.QUALIFICATION)).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesBySalesStage(SalesStage.QUALIFICATION));
        verify(repository, times(1)).findBySalesStage(SalesStage.QUALIFICATION);
    }

    @Test
    void getOpportunitiesByEstimatedValue_success() {
        List<SalesOpportunity> opportunityList = new ArrayList<>();
        opportunityList.add(salesOpportunity);
        when(repository.findByEstimatedValue(BigDecimal.valueOf(1000))).thenReturn(opportunityList);
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByEstimatedValue(BigDecimal.valueOf(1000));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(repository, times(1)).findByEstimatedValue(BigDecimal.valueOf(1000));
    }

    @Test
    void getOpportunitiesByEstimatedValue_estimatedValueNotFound_throwsNoSuchElementException() {
        when(repository.findByEstimatedValue(BigDecimal.valueOf(1000))).thenReturn(new ArrayList<>());
        BigDecimal bigDecimal = BigDecimal.valueOf(1000);
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByEstimatedValue(bigDecimal));
        verify(repository, times(1)).findByEstimatedValue(BigDecimal.valueOf(1000));
    }

    @Test
    void getOpportunitiesByClosingDate_success() {
        List<SalesOpportunity> opportunityList = new ArrayList<>();
        opportunityList.add(salesOpportunity);
        when(repository.findByClosingDate(LocalDate.now().plusDays(30))).thenReturn(opportunityList);
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByClosingDate(LocalDate.now().plusDays(30));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(repository, times(1)).findByClosingDate(LocalDate.now().plusDays(30));
    }

    @Test
    void getOpportunitiesByClosingDate_closingDateNotFound_throwsNoSuchElementException() {
        when(repository.findByClosingDate(LocalDate.now().plusDays(30))).thenReturn(new ArrayList<>());
        LocalDate localDate = LocalDate.now().plusDays(30);
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByClosingDate(localDate));
        verify(repository, times(1)).findByClosingDate(LocalDate.now().plusDays(30));
    }

    @Test
    void getOpportunitiesByFollowUpReminder_success() {
        List<SalesOpportunity> opportunityList = new ArrayList<>();
        opportunityList.add(salesOpportunity);
        when(repository.findByFollowUpReminder(LocalDate.now().plusDays(7))).thenReturn(opportunityList);
        List<SalesOpportunityResponseDTO> result = service.getOpportunitiesByFollowUpReminder(LocalDate.now().plusDays(7));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(repository, times(1)).findByFollowUpReminder(LocalDate.now().plusDays(7));
    }
    @Test
    void getOpportunitiesByFollowUpReminder_followUpReminderNotFound_throwsNoSuchElementException() {
        when(repository.findByFollowUpReminder(LocalDate.now().plusDays(7))).thenReturn(new ArrayList<>());
        LocalDate localDate = LocalDate.now().plusDays(7);
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByFollowUpReminder(localDate));
        verify(repository, times(1)).findByFollowUpReminder(LocalDate.now().plusDays(7));
    }

    @Test
    void scheduleFollowUpReminder_success() throws InvalidDateTimeException, InvalidOpportunityIdException {
        when(repository.findById(1L)).thenReturn(Optional.of(salesOpportunity));
        when(repository.save(any(SalesOpportunity.class))).thenReturn(salesOpportunity);
        SalesOpportunityResponseDTO result = service.scheduleFollowUpReminder(1L, LocalDate.now().plusDays(7));
        assertEquals(responseDTO, result);
        assertEquals(LocalDate.now().plusDays(7), result.getFollowUpReminder());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(SalesOpportunity.class));
    }

    @Test
    void scheduleFollowUpReminder_reminderDateInPast_throwsInvalidDateTimeException() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        assertThrows(InvalidDateTimeException.class, () -> service.scheduleFollowUpReminder(1L, localDate));
        verify(repository, never()).findById(anyLong());
        verify(repository, never()).save(any(SalesOpportunity.class));
    }

    @Test
    void scheduleFollowUpReminder_opportunityNotFound_throwsInvalidOpportunityIdException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        LocalDate localDate = LocalDate.now().plusDays(10);
        assertThrows(InvalidOpportunityIdException.class, () -> service.scheduleFollowUpReminder(1L, localDate));
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any(SalesOpportunity.class));
    }

    @Test
    void deleteByOpportunityID_success() throws InvalidOpportunityIdException {
        when(repository.findById(1L)).thenReturn(Optional.of(salesOpportunity));
        boolean result = service.deleteByOpportunityID(1L);
        assertTrue(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(salesOpportunity);
    }

    @Test
    void deleteByOpportunityID_opportunityNotFound_throwsInvalidOpportunityIdException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidOpportunityIdException.class, () -> service.deleteByOpportunityID(1L));
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).delete(any(SalesOpportunity.class));
    }
}