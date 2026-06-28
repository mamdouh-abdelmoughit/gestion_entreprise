package com.btp.repository;

import com.btp.entity.AffectationEmploye;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffectationEmployeRepository extends JpaRepository<AffectationEmploye, Long> {
    long countByProjet(Projet projet);
    
    void deleteByProjet(Projet projet);
    
    long countByEmploye(Employe employe);
    
    void deleteByEmploye(Employe employe);
}
