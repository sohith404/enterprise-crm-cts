package com.crm.repository;
import com.crm.entities.Campaign;
import com.crm.enums.Type;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing {@link Campaign} entities.
 * Extends {@link JpaRepository} to inherit basic CRUD operations.
 * Provides custom query methods for retrieving campaigns based on specific criteria.
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	/**
     * Finds all campaigns with the specified {@link Type}.
     *
     * @param type The {@link Type} of campaigns to retrieve.
     * @return A list of {@link Campaign} entities matching the given type.
     */
	List<Campaign> findByType(Type type);
}