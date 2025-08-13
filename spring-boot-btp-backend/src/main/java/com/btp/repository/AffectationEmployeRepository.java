package com.btp.repository;

import com.btp.entity.AffectationEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffectationEmployeRepository extends JpaRepository<AffectationEmploye, Long> {
}
