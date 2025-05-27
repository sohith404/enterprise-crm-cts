package com.crm.controller;

import com.crm.dto.ReportResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.enums.ReportType;
import com.crm.exception.InvalidDataRecievedException;
import com.crm.exception.NoReportsAvailableException;
import com.crm.scheduler.DynamicSchedulerService;
import com.crm.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/analytics")
public class ReportControllerImpl implements ReportController{

    private final ReportService reportService;
    private final DynamicSchedulerService schedulerService;
    private static final String ERROR_RESPONSE = "No Reports Available";

    @Autowired
    ReportControllerImpl(ReportService reportService, DynamicSchedulerService schedulerService){
        this.reportService = reportService;
        this.schedulerService = schedulerService;
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateCustomersReport() throws InvalidDataRecievedException, JsonProcessingException {
            ReportResponseDTO reportResponseDTO = reportService.generateCustomerReport();
            return new ResponseEntity<>(reportResponseDTO, HttpStatus.OK);
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateSalesReport() throws InvalidDataRecievedException, JsonProcessingException {


        ReportResponseDTO reportResponseDTO = reportService.generateSalesReport();
        return ResponseEntity.ok(reportResponseDTO);

    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateSupportTicketsReport() throws InvalidDataRecievedException, JsonProcessingException {

            ReportResponseDTO reportResponseDTO = reportService.generateSupportReport();
            return ResponseEntity.ok(reportResponseDTO);

    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> generateMarketingReport() throws InvalidDataRecievedException, JsonProcessingException {

            ReportResponseDTO reportResponseDTO = reportService.generateMarketingReport();
            return ResponseEntity.ok(reportResponseDTO);

    }
    /**
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<ReportResponseDTO> getReportById(Long id) {
        ReportResponseDTO reportResponseDTO = reportService.getReportById(id);
        return ResponseEntity.ok(reportResponseDTO);
    }

    /**
     * @param scheduleConfigRequestDTO
     * @return
     */
    @Override
    public ResponseEntity<ScheduleConfigResponseDTO> configCronJob(ScheduleConfigRequestDTO scheduleConfigRequestDTO) {
        ScheduleConfigResponseDTO result = schedulerService.updateCronExpression(scheduleConfigRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ReportResponseDTO>> getReportByType(String type) {
        List<ReportResponseDTO> result = reportService.getReportByType(ReportType.valueOf(type));
        return ResponseEntity.ok(result);
    }
    @Override
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        List<ReportResponseDTO> reportResponseDTOs = reportService.getAllReports();
        if(reportResponseDTOs.isEmpty()){
            throw new NoReportsAvailableException(ERROR_RESPONSE);
        }
        return ResponseEntity.ok(reportResponseDTOs);
    }
}
