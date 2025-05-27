package com.crm.repository;

import com.crm.entities.Agents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentsRepository extends JpaRepository<Agents, Long> {
	
}