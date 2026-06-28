package com.btp.repository;

import com.btp.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByCode(String code);
    Optional<Organization> findByName(String name);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
