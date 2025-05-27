package com.crm.repository;

import com.crm.entities.Report;
import com.crm.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportType(ReportType reportType);

}