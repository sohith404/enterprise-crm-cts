package com.crm.repository;

import com.crm.entities.SupportTicket;
import com.crm.enums.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

	List <SupportTicket> findByCustomerID(long l);

	List <SupportTicket> findByStatus(Status status);
	
}