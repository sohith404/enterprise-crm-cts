package com.crm.controller;

import com.crm.dto.ReportResponseDTO;
import com.crm.dto.ScheduleConfigRequestDTO;
import com.crm.dto.ScheduleConfigResponseDTO;
import com.crm.exception.InvalidDataRecievedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public interface ReportController {

    @PostMapping("/customers")
    public ResponseEntity<ReportResponseDTO> generateCustomersReport() throws InvalidDataRecievedException, JsonProcessingException;

    @PostMapping("/sales")
    public ResponseEntity<ReportResponseDTO> generateSalesReport() throws InvalidDataRecievedException, JsonProcessingException;

    @PostMapping("/supportTickets")
    public ResponseEntity<ReportResponseDTO> generateSupportTicketsReport() throws InvalidDataRecievedException, JsonProcessingException;

    @PostMapping("/marketingCampaigns")
    public ResponseEntity<ReportResponseDTO> generateMarketingReport() throws InvalidDataRecievedException, JsonProcessingException;

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id);

    @GetMapping("type/{type}")
    public ResponseEntity<List<ReportResponseDTO>> getReportByType(@PathVariable String type);

    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAllReports();

    @PostMapping(value = "/configureCron")
    ResponseEntity<ScheduleConfigResponseDTO> configCronJob(@Valid @RequestBody ScheduleConfigRequestDTO scheduleConfigRequestDTO);
}
