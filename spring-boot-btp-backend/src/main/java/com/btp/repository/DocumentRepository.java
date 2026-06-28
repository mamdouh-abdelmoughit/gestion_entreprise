package com.btp.repository;

import com.btp.entity.Document;
import com.btp.entity.Employe;
import com.btp.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    long countByProjet(Projet projet);
    
    void deleteByProjet(Projet projet);
    
    long countByEmploye(Employe employe);
}
