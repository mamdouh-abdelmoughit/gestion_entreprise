package com.btp.repository;

import com.btp.entity.Depense;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    long countByProjet(Projet projet);
    
    void deleteByProjet(Projet projet);
    
    long countByEmploye(Employe employe);
    
    long countByFournisseur(com.btp.entity.Fournisseur fournisseur);
}
