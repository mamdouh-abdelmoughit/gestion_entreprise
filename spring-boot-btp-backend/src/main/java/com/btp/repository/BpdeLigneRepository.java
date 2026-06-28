package com.btp.repository;

import com.btp.entity.BpdeLigne;
import com.btp.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BpdeLigneRepository extends JpaRepository<BpdeLigne, Long> {
    List<BpdeLigne> findByProjetOrderByOrdreAsc(Projet projet);
    List<BpdeLigne> findByProjetId(Long projetId);
}
