package com.btp.repository;

import com.btp.entity.Client;
import com.btp.entity.Organization;
import com.btp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserAccount(User userAccount);
    
    // Multi-tenancy queries
    Page<Client> findByOrganization(Organization organization, Pageable pageable);
    Page<Client> findByOrganizationId(Long organizationId, Pageable pageable);
    Optional<Client> findByIdAndOrganizationId(Long id, Long organizationId);
}