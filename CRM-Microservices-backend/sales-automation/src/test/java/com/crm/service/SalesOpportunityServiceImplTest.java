package com.crm.service;

import com.crm.dto.CustomerProfileDTO;
import com.crm.dto.NotificationDTO;
import com.crm.dto.SalesOpportunityRequestDTO;
import com.crm.dto.SalesOpportunityResponseDTO;
import com.crm.entities.EmailFormat;
import com.crm.entities.SalesOpportunity;
import com.crm.enums.SalesStage;
import com.crm.exception.CustomerNotFoundException;
import com.crm.exception.InvalidDateTimeException;
import com.crm.exception.InvalidOpportunityIdException;
import com.crm.feign.Proxy;
import com.crm.mapper.SalesOpportunityMapper;
import com.crm.repository.SalesOpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SalesOpportunityServiceImplTest {


    @Mock
    private SalesOpportunityRepository repository;

    @Mock
    private SalesOpportunityMapper mapper;

    @Mock
    private Proxy proxy;


    @InjectMocks
    private SalesOpportunityServiceImpl service;

    private static List<SalesOpportunity> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                SalesOpportunity.builder()
                        .opportunityID(1L)
                        .customerID(1L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(2L)
                        .customerID(2L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                        .build(),
                SalesOpportunity.builder()
                        .opportunityID(3L)
                        .customerID(3L)
                        .estimatedValue(new BigDecimal("10000.0"))
                        .salesStage(SalesStage.PROSPECTING)
                        .closingDate(LocalDate.of(2025, Month.MAY, 18))
                        .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                        .build()
        ));
    }

    @Test
    @DisplayName("retrieveAllSalesOpportunities() - Positive")
    void retrieveAllSalesOpportunitiesShouldReturnListOfOpportunitiesWhenDataExists() {
        when(repository.findAll()).thenReturn(list);
        assertFalse(service.retrieveAllSalesOpportunities().isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("retrieveAllSalesOpportunities() - Negative")
    void retrieveAllSalesOpportunitiesShouldThrowExceptionWhenNoDataExists() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> service.retrieveAllSalesOpportunities());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("createSalesOpportunity() - Positive")
    void createSalesOpportunitieyShouldReturnSalesOpportunityResponseDTO(){
        when(proxy.getCustomerById(1L)).thenReturn(new ResponseEntity<>(new CustomerProfileDTO(), HttpStatus.OK));
        SalesOpportunity salesOpportunity = SalesOpportunity.builder()
                .customerID(1L)
                .estimatedValue(BigDecimal.valueOf(10000.0))
                .salesStage(SalesStage.PROSPECTING)
                .closingDate(LocalDate.of(2025, Month.MAY, 18))
                .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                .build();
        when(repository.save(any(SalesOpportunity.class))).thenReturn(list.getFirst());
        SalesOpportunityRequestDTO salesOpportunityRequestDTO = SalesOpportunityRequestDTO
                .builder()
                .salesStage(SalesStage.PROSPECTING)
                .estimatedValue(BigDecimal.valueOf(10000.0))
                .closingDate(LocalDate.of(2025, Month.MAY, 18))
                .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                .customerID(salesOpportunity.getCustomerID())
                .build();
        SalesOpportunityResponseDTO salesOpportunityResponseDTO = SalesOpportunityResponseDTO
                .builder()
                .opportunityID(1L)
                .salesStage(SalesStage.PROSPECTING)
                .estimatedValue(BigDecimal.valueOf(10000.0))
                .closingDate(LocalDate.of(2025, Month.MAY, 18))
                .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                .customerID(salesOpportunity.getCustomerID())
                .build();
        assertEquals(salesOpportunityResponseDTO, service.createSalesOpportunity(salesOpportunityRequestDTO));
    }

    @Test
    @DisplayName("createSalesOpportunity() - Negative")
    void createSalesOpportunitieyShouldThrowExceptionWhenProxyReturnsNUll(){
        when(proxy.getCustomerById(1L)).thenReturn(null);
        SalesOpportunityRequestDTO salesOpportunityRequestDTO = SalesOpportunityRequestDTO
                .builder()
                .salesStage(SalesStage.PROSPECTING)
                .estimatedValue(BigDecimal.valueOf(10000.0))
                .closingDate(LocalDate.of(2025, Month.MAY, 18))
                .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                .customerID(1L)
                .build();
        assertThrows(CustomerNotFoundException.class, () -> service.createSalesOpportunity(salesOpportunityRequestDTO));
    }


    @Test
    @DisplayName("createSalesOpportunity() - Negative")
    void createSalesOpportunitieyShouldThrowExceptionWhenProxyReturnErrorResponseDTO(){
        when(proxy.getCustomerById(1L)).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        SalesOpportunityRequestDTO salesOpportunityRequestDTO = SalesOpportunityRequestDTO
                .builder()
                .salesStage(SalesStage.PROSPECTING)
                .estimatedValue(BigDecimal.valueOf(10000.0))
                .closingDate(LocalDate.of(2025, Month.MAY, 18))
                .followUpReminder(LocalDate.of(2025, Month.APRIL, 18))
                .customerID(1L)
                .build();
        assertThrows(CustomerNotFoundException.class, () -> service.createSalesOpportunity(salesOpportunityRequestDTO));
    }



    @Test
    @DisplayName("updateSalesOpportunity() - Positive")
    void updateOpportunityShouldUpdateAndReturnOpportunityWhenIdExists(){
        SalesOpportunity salesOpportunity = SalesOpportunity.builder()
                .opportunityID(1L)
                .customerID(1L)
                .estimatedValue(BigDecimal.valueOf(10000.0))
                .salesStage(SalesStage.PROSPECTING)
                .closingDate(LocalDate.now().plusDays(8))
                .followUpReminder(LocalDate.now().plusDays(14))
                .build();
        SalesOpportunity newSalesOpportunity = SalesOpportunity.builder()
                .customerID(1L)
                .salesStage(SalesStage.CLOSED_WON)
                .estimatedValue(BigDecimal.valueOf(20000.0))
                .closingDate(LocalDate.now().plusDays(8))
                .followUpReminder(LocalDate.now().plusDays(14))
                .build();
        SalesOpportunityRequestDTO salesOpportunityRequestDTO = SalesOpportunityRequestDTO
                .builder()
                .salesStage(SalesStage.CLOSED_WON)
                .estimatedValue(BigDecimal.valueOf(20000.0))
                .closingDate(newSalesOpportunity.getClosingDate())
                .customerID(newSalesOpportunity.getCustomerID())
                .followUpReminder(newSalesOpportunity.getFollowUpReminder())
                .build();
        SalesOpportunityResponseDTO salesOpportunityResponseDTO = SalesOpportunityResponseDTO
                .builder()
                .opportunityID(1L)
                .salesStage(SalesStage.CLOSED_WON)
                .estimatedValue(BigDecimal.valueOf(20000.0))
                .closingDate(newSalesOpportunity.getClosingDate())
                .customerID(newSalesOpportunity.getCustomerID())
                .followUpReminder(newSalesOpportunity.getFollowUpReminder())
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(salesOpportunity));
        when(repository.save(any(SalesOpportunity.class))).thenAnswer((invocation -> {
            newSalesOpportunity.setOpportunityID(1L);
            return newSalesOpportunity;
        }));
        assertEquals(salesOpportunityResponseDTO, service.updateSalesOpportunity(1L, salesOpportunityRequestDTO));
        verify(repository, times(2)).findById(1L);
        verify(repository, times(2)).save(any(SalesOpportunity.class));
    }

    @Test
    @DisplayName("updateSalesOpportunity() - Negative")
    void updateOpportunityShouldThrowExceptionWhenIdDoesNotExists(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        SalesOpportunityRequestDTO salesOpportunityRequestDTO = SalesOpportunityRequestDTO
                .builder()
                .build();
        assertThrows(NoSuchElementException.class,() -> service.updateSalesOpportunity(1L, salesOpportunityRequestDTO));
    }

    @Test
    @DisplayName("updateSalesStage() - Positive")
    void updateSalesStageShouldReturnUpdatedDTOWhenIdExists(){
        SalesOpportunity salesOpportunity = list.getFirst();
        SalesOpportunityResponseDTO salesOpportunityResponseDTO = SalesOpportunityResponseDTO
                .builder()
                .opportunityID(1L)
                .salesStage(SalesStage.CLOSED_WON)
                .estimatedValue(salesOpportunity.getEstimatedValue())
                .closingDate(LocalDate.now())
                .customerID(salesOpportunity.getCustomerID())
                .followUpReminder(salesOpportunity.getFollowUpReminder())
                .build();

        EmailFormat email = EmailFormat.builder()
                .salutation("Dear employee,")
                .openingLine("I hope this message finds you well.")
                .body("This is to inform you that Sales Stage for Lead #" + 1 + " is changed to " + SalesStage.CLOSED_WON.name() + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ".")
                .conclusion("THIS IS AN AUTO-GENERATED EMAIL. PLEASE DO NOT REPLY ON THIS!")
                .closing("SALES-AUTOMATION SERVICE \n CRM")
                .build();

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .subject("Sales Status Changed for Lead with ID " + 1)
                .body(email.toString())
                .status("SENT")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(salesOpportunity));
        when(proxy.sendNotificaton(any(NotificationDTO.class))).thenReturn(notificationDTO);
        when(repository.save(any(SalesOpportunity.class))).thenReturn(salesOpportunity);
        assertEquals(salesOpportunityResponseDTO, service.updateSalesStage(1L , SalesStage.CLOSED_WON));
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(SalesOpportunity.class));
        verify(proxy, times(1)).sendNotificaton(any(NotificationDTO.class));
    }

    @Test
    @DisplayName("updateSalesStage() - Negative")
    void updateSalesStageShouldThrowException(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,() -> service.updateSalesStage(1L, SalesStage.CLOSED_WON));
    }

    @Test
    @DisplayName("getOpportunitiesByOpportunityID() - Positive")
    void getOpportunitiesByOpportunityIdShouldReturnOpportunityWhenIdExists() {
        when(repository.findById(1L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 1L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));

        SalesOpportunityResponseDTO salesOpportunityResponseDTO = service.getOpportunitiesByOpportunity(1L);

        assertEquals(1L, (long) salesOpportunityResponseDTO.getOpportunityID());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getOpportunitiesByOpportunityID() - Negative")
    void getOpportunitiesByOpportunityIdShouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(0L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 0L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByOpportunity(0L));
        verify(repository, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("getOpportunitiesByCustomer() - Positive")
    void getOpportunitiesByCustomerIdShouldReturnListOfOpportunitiesWhenCustomerExists() {
        when(repository.findByCustomerID(1L)).thenAnswer((invocation -> {
            List<SalesOpportunity> salesOpportunityList = new ArrayList<>();
            for (SalesOpportunity s : list) {
                if (s.getCustomerID() == 1L) {
                    salesOpportunityList.add(s);
                }
            }
            return salesOpportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByCustomer = service.getOpportunitiesByCustomer(1L);

        assertFalse(opportunitiesByCustomer.isEmpty());
        verify(repository, times(1)).findByCustomerID(1L);
    }

    @Test
    @DisplayName("getOpportunitiesByCustomer() - Negative")
    void getOpportunitiesByCustomerIdShouldThrowExceptionWhenCustomerDoesNotExist() {
        when(repository.findByCustomerID(0L)).thenAnswer((invocation -> {
            List<SalesOpportunity> salesOpportunityList = new ArrayList<>();
            for (SalesOpportunity s : list) {
                if (s.getCustomerID() == 0L) {
                    salesOpportunityList.add(s);
                }
            }
            return salesOpportunityList;
        }));
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByCustomer(0L));
        verify(repository, times(1)).findByCustomerID(anyLong());
    }

    @Test
    @DisplayName("getOpportunitiesBySalesStage() - Positive")
    void getOpportunitiesBySalesStageShouldReturnListOfOpportunitiesWhenSalesStageExists() {
        when(repository.findBySalesStage(SalesStage.PROSPECTING)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getSalesStage() == SalesStage.PROSPECTING) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesBySalesStage = service.getOpportunitiesBySalesStage(SalesStage.PROSPECTING);

        assertFalse(opportunitiesBySalesStage.isEmpty());
        verify(repository, times(1)).findBySalesStage(any(SalesStage.class));
    }

    @Test
    @DisplayName("getOpportunitiesBySalesStage() - Negative")
    void getOpportunitiesBySalesStageShouldThrowExceptionWhenSalesStageDoesNotExist() {
        when(repository.findBySalesStage(SalesStage.CLOSED_LOST)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getSalesStage() == SalesStage.CLOSED_LOST) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesBySalesStage(SalesStage.CLOSED_LOST));
        verify(repository, times(1)).findBySalesStage(any(SalesStage.class));
    }

    @Test
    @DisplayName("getOpportunitiesByEstimatedValue() - Positive")
    void getOpportunitiesByEstimatedValueShouldReturnListOfOpportunitiesWhenEstimatedValueExists() {
        when(repository.findByEstimatedValue(new BigDecimal("10000.0"))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (Objects.equals(e.getEstimatedValue(), new BigDecimal("10000.0"))) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByEstimatedValue = service.getOpportunitiesByEstimatedValue(new BigDecimal("10000.0"));
        verify(repository, times(1)).findByEstimatedValue(new BigDecimal("10000.0"));
        assertFalse(opportunitiesByEstimatedValue.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByEstimatedValue() - Negative")
    void getOpportunitiesByEstimatedValueShouldThrowExceptionWhenEstimatedValueDoesNotExist() {
        BigDecimal bigDecimal = new BigDecimal("20000.0");
        when(repository.findByEstimatedValue(bigDecimal)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getEstimatedValue().equals(bigDecimal)) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));
        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByEstimatedValue(bigDecimal));
        verify(repository, times(1)).findByEstimatedValue(bigDecimal);
    }

    @Test
    @DisplayName("getOpportunitiesByClosingDate() - Positive")
    void getOpportunitiesByClosingDateShouldReturnListOfOpportunitiesWhenClosingDateExists() {
        when(repository.findByClosingDate(LocalDate.of(2025, Month.MAY, 18))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getClosingDate().equals(LocalDate.of(2025, Month.MAY, 18))) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByClosingDate = service.getOpportunitiesByClosingDate(LocalDate.of(2025, Month.MAY, 18));
        verify(repository, times(1)).findByClosingDate(LocalDate.of(2025, Month.MAY, 18));
        assertFalse(opportunitiesByClosingDate.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByClosingDate() - Negative")
    void getOpportunitiesByClosingDateShouldThrowExceptionWhenClosingDateDoesNotExist() {
        LocalDate localDate = LocalDate.of(2025, Month.MAY, 20);
        when(repository.findByClosingDate(localDate)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getClosingDate().equals(localDate)) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByClosingDate(localDate));
        verify(repository, times(1)).findByClosingDate(localDate);
    }

    @Test
    @DisplayName("getOpportunitiesByFollowUpReminder() - Positive")
    void getOpportunitiesByFollowUpReminderShouldReturnListOfOpportunitiesWhenFollowUpReminderExists() {
        when(repository.findByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18))).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getFollowUpReminder().equals(LocalDate.of(2025, Month.APRIL, 18))) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        List<SalesOpportunityResponseDTO> opportunitiesByFollowUpReminder = service.getOpportunitiesByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18));
        verify(repository, times(1)).findByFollowUpReminder(LocalDate.of(2025, Month.APRIL, 18));
        assertFalse(opportunitiesByFollowUpReminder.isEmpty());
    }

    @Test
    @DisplayName("getOpportunitiesByFollowUpReminder() - Negative")
    void getOpportunitiesByFollowUpReminderShouldThrowExceptionWhenFollowUpReminderDoesNotExist() {
        LocalDate localDate = LocalDate.of(2025, Month.APRIL, 28);
        when(repository.findByFollowUpReminder(localDate)).thenAnswer((invocation -> {
            List<SalesOpportunity> opportunityList = new ArrayList<>();
            list.forEach(e -> {
                if (e.getFollowUpReminder().isEqual(localDate)) {
                    opportunityList.add(e);
                }
            });
            return opportunityList;
        }));

        assertThrows(NoSuchElementException.class, () -> service.getOpportunitiesByFollowUpReminder(localDate));
        verify(repository, times(1)).findByFollowUpReminder(localDate);
    }


    @Test
    @DisplayName("scheduleFollowUpReminder() - Positive")
    void scheduleFollowUpReminderShouldUpdateAndReturnOpportunityWhenValidInput() {
        SalesOpportunity salesOpportunity = list.getFirst();
        LocalDate localDate = LocalDate.of(2025, Month.APRIL, 30);
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(salesOpportunity));
        assert salesOpportunity != null;
        when(repository.save(salesOpportunity)).thenAnswer(invocation -> {
            salesOpportunity.setFollowUpReminder(localDate);
            return salesOpportunity;
        });
        assertEquals(localDate, service.scheduleFollowUpReminder(1L, localDate).getFollowUpReminder());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("scheduleFollowUpReminder() - Negative_InvalidDateTimeException")
    void scheduleFollowUpReminderShouldThrowExceptionWhenInvalidDateTime() {
        LocalDate localDate = LocalDate.of(2020, Month.JANUARY, 18);

        assertThrows(InvalidDateTimeException.class, () -> service.scheduleFollowUpReminder(1L, localDate));
        verify(repository, times(0)).findById(anyLong());
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("scheduleFollowUpReminder() - Negative_InvalidOpportunityIDException")
    void scheduleFollowUpReminderShouldThrowExceptionWhenInvalidOpportunityId() {
        when(repository.findById(0L)).thenReturn(Optional.empty());
        LocalDate localDate = LocalDate.of(2026, Month.JANUARY, 18);
        assertThrows(InvalidOpportunityIdException.class, () -> service.scheduleFollowUpReminder(0L, localDate));
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("testDeleteByOpportunityID() - Positive")
    void deleteByOpportunityIdShouldRemoveOpportunityWhenIdExists() {
        SalesOpportunity obj = list.getFirst();
        when(repository.findById(1L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 1L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));
        doAnswer(invocation -> list.remove(obj)).when(repository).delete(obj);
        service.deleteByOpportunityID(1L);
        Optional<SalesOpportunity> salesOpportunity = repository.findById(1L);
        assertFalse(salesOpportunity.isPresent());
        verify(repository, times(1)).delete(obj);
        verify(repository, times(2)).findById(1L);
    }

    @Test
    @DisplayName("testDeleteByOpportunityID() - Negative")
    void deleteByOpportunityIdShouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(0L)).thenAnswer((invocation -> {
            for (SalesOpportunity s : list) {
                if (s.getOpportunityID() == 0L) {
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        }));
        assertThrows(InvalidOpportunityIdException.class, () -> service.deleteByOpportunityID(0L));
        verify(repository, times(0)).delete(any(SalesOpportunity.class));
        verify(repository, times(1)).findById(0L);
    }

}