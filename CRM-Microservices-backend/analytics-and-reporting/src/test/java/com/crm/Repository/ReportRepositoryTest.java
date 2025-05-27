package com.crm.Repository;

import com.crm.entities.Report;
import com.crm.enums.ReportType;
import com.crm.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DataJpaTest
class ReportRepositoryTest {

    @Mock
    private ReportRepository reportRepository;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSaveReport() {
        Report report = Report.builder()
                .id(1L)
                .reportType(ReportType.CUSTOMER)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Customer data points")
                .build();
        when(reportRepository.save(report)).thenReturn(report);

        Report savedReport = reportRepository.save(report);

        assertEquals("Customer data points", savedReport.getDataPoints());
        assertEquals(ReportType.CUSTOMER, savedReport.getReportType());
    }

    @Test
     void testFindById() {
        Report report = Report.builder()
                .id(1L)
                .reportType(ReportType.SUPPORT)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Support data points")
                .build();
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        Optional<Report> foundReport = reportRepository.findById(1L);

        assertTrue(foundReport.isPresent());
        assertEquals("Support data points", foundReport.get().getDataPoints());
    }

    @Test
     void testUpdateReport() {
        Report report = Report.builder()
                .id(1L)
                .reportType(ReportType.MARKETING)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Marketing data points")
                .build();
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(reportRepository.save(report)).thenReturn(report);

        report.setDataPoints("Updated Marketing data points");
        Report updatedReport = reportRepository.save(report);

        assertEquals("Updated Marketing data points", updatedReport.getDataPoints());
    }

    @Test
    void testDeleteReport() {
        Report.builder()
                .id(1L)
                .reportType(ReportType.SALES)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Sales data points")
                .build();
        doNothing().when(reportRepository).deleteById(1L);

        reportRepository.deleteById(1L);

        verify(reportRepository, times(1)).deleteById(1L);
    }

    @Test
     void testFindByReportType() {
        ReportType reportType = ReportType.SUPPORT;
        Report report1 = Report.builder()
                .id(1L)
                .reportType(reportType)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Support data points Q1")
                .build();
        Report report2 = Report.builder()
                .id(2L)
                .reportType(reportType)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Support data points Q2")
                .build();
        List<Report> reports = Arrays.asList(report1, report2);

        when(reportRepository.findByReportType(reportType)).thenReturn(reports);

        List<Report> foundReports = reportRepository.findByReportType(reportType);

        assertEquals(2, foundReports.size());
        assertEquals("Support data points Q1", foundReports.get(0).getDataPoints());
        assertEquals("Support data points Q2", foundReports.get(1).getDataPoints());
    }
    @Test
    void testFindAllReports() {
        Report report1 = Report.builder()
                .id(1L)
                .reportType(ReportType.CUSTOMER)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Customer data points")
                .build();
        Report report2 = Report.builder()
                .id(2L)
                .reportType(ReportType.SALES)
                .generatedDate(LocalDateTime.now())
                .dataPoints("Sales data points")
                .build();
        List<Report> reports = Arrays.asList(report1, report2);

        when(reportRepository.findAll()).thenReturn(reports);

        List<Report> foundReports = reportRepository.findAll();

        assertEquals(2, foundReports.size());
        assertEquals("Customer data points", foundReports.get(0).getDataPoints());
        assertEquals("Sales data points", foundReports.get(1).getDataPoints());
    }

    @Test
    void testDeleteAllReports() {
        doNothing().when(reportRepository).deleteAll();

        reportRepository.deleteAll();

        verify(reportRepository, times(1)).deleteAll();
    }
}