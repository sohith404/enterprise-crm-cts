package com.crm.repository;

import com.crm.entities.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link CustomerProfile} entity.
 */

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    /**
     * Finds a customer profile by email.
     *
     * @param email the email of the customer profile to find
     * @return an Optional containing the found customer profile, or empty if not found
     */
    @Query("SELECT c FROM CustomerProfile c WHERE c.emailId = :email")
    Optional<CustomerProfile> findByEmail(@Param("email") String email);

    /**
     * Finds a customer profile by contact number.
     *
     * @param contactNumber the contact number of the customer profile to find
     * @return an Optional containing the found customer profile, or empty if not found
     */
    @Query("SELECT c FROM CustomerProfile c WHERE c.phoneNumber = :contactNumber")
    Optional<CustomerProfile> findByContactNumber(@Param("contactNumber") String contactNumber);

    /**
     * Finds a customer profile by name containing the specified string.
     *
     * @param name the name string to search for
     * @return an Optional containing the found customer profile, or empty if not found
     */
    @Query("SELECT c FROM CustomerProfile c WHERE c.name LIKE %:name%")
    Optional<CustomerProfile> findByNameContaining(@Param("name") String name);

    /**
     * Finds all customer profiles by email.
     *
     * @param email the email of the customer profiles to find
     * @return a list of customer profiles with the specified email
     */
    @Query("SELECT c FROM CustomerProfile c WHERE c.emailId = :email")
    List<CustomerProfile> findAllByEmailId(@Param("email") String email);

    /**
     * Finds all customer profiles by contact number.
     *
     * @param contactNumber the contact number of the customer profiles to find
     * @return a list of customer profiles with the specified contact number
     */
    @Query("SELECT c FROM CustomerProfile c WHERE c.phoneNumber = :contactNumber")
    List<CustomerProfile> findAllByPhoneNumber(@Param("contactNumber") String contactNumber);

    /**
     * Finds all customer profiles by name containing the specified string.
     *
     * @param name the name string to search for
     * @return a list of customer profiles with names containing the specified string
     */
    @Query("SELECT c FROM CustomerProfile c WHERE c.name LIKE %:name%")
    List<CustomerProfile> findAllByNames(@Param("name") String name);
}