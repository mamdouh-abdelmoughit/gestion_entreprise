package com.btp.service;

import com.btp.dto.FactureDTO;
import com.btp.entity.*;
import com.btp.exception.BadRequestException;
import com.btp.exception.ResourceNotFoundException;
import com.btp.repository.AttachementRepository;
import com.btp.repository.FactureRepository;
import com.btp.repository.PaiementRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FactureService {

    @Autowired private FactureRepository factureRepository;
    @Autowired private ProjetRepository projetRepository;
    @Autowired private AttachementRepository attachementRepository;
    @Autowired private PaiementRepository paiementRepository;
    @Autowired private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<FactureDTO> findAll(Pageable pageable) {
        return factureRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public FactureDTO findById(Long id) {
        return factureRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Facture not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<FactureDTO> findByProjet(Long projetId, Pageable pageable) {
        Projet p = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + projetId));
        return factureRepository.findByProjet(p, pageable).map(this::toDTO);
    }

    public FactureDTO save(FactureDTO dto) {
        if (factureRepository.existsByNumero(dto.getNumero())) {
            throw new BadRequestException("Numéro de facture déjà utilisé: " + dto.getNumero());
        }
        Facture entity = new Facture();
        mapDtoToEntity(dto, entity);
        entity.setCreatedBy(currentUser());
        return toDTO(factureRepository.save(entity));
    }

    /**
     * Generate a Facture from an Attachement using the project's BPDE unit prices.
     * Each LigneAttachement is matched to its linked BpdeLigne.
     */
    public FactureDTO generateFromAttachement(Long attachementId, String numero, Double tva,
                                               Double retenuGarantie, Double avance) {
        Attachement att = attachementRepository.findById(attachementId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachement not found: " + attachementId));

        if (factureRepository.existsByNumero(numero)) {
            throw new BadRequestException("Numéro de facture déjà utilisé: " + numero);
        }

        double montantHT = att.getLignes().stream()
                .filter(l -> l.getBpdeLigne() != null)
                .mapToDouble(l -> l.getQuantiteRealisee() * l.getBpdeLigne().getPrixUnitaire())
                .sum();

        double tvaRate = (tva != null) ? tva : 20.0;
        double montantTTC = montantHT * (1 + tvaRate / 100);
        double rg = (retenuGarantie != null) ? retenuGarantie : 0.0;
        double av = (avance != null) ? avance : 0.0;
        double montantNet = montantTTC - rg - av;

        Facture facture = new Facture();
        facture.setNumero(numero);
        facture.setProjet(att.getProjet());
        facture.setAttachement(att);
        facture.setType(Facture.TypeFacture.CLIENT);
        facture.setDateEmission(java.time.LocalDate.now());
        facture.setDateEcheance(java.time.LocalDate.now().plusDays(30));
        facture.setMontantHT(montantHT);
        facture.setTva(tvaRate);
        facture.setMontantTTC(montantTTC);
        facture.setRetenuGarantie(rg);
        facture.setAvance(av);
        facture.setMontantNet(montantNet);
        facture.setStatut(Facture.StatutFacture.BROUILLON);
        facture.setCreatedBy(currentUser());

        return toDTO(factureRepository.save(facture));
    }

    public FactureDTO update(Long id, FactureDTO dto) {
        Facture entity = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture not found: " + id));
        mapDtoToEntity(dto, entity);
        return toDTO(factureRepository.save(entity));
    }

    public void delete(Long id) {
        factureRepository.deleteById(id);
    }

    private void mapDtoToEntity(FactureDTO dto, Facture entity) {
        entity.setNumero(dto.getNumero());
        entity.setDateEmission(dto.getDateEmission());
        entity.setDateEcheance(dto.getDateEcheance());
        entity.setMontantHT(dto.getMontantHT() != null ? dto.getMontantHT() : 0.0);
        entity.setTva(dto.getTva() != null ? dto.getTva() : 20.0);
        entity.setMontantTTC(dto.getMontantTTC() != null ? dto.getMontantTTC() : 0.0);
        entity.setRetenuGarantie(dto.getRetenuGarantie() != null ? dto.getRetenuGarantie() : 0.0);
        entity.setAvance(dto.getAvance() != null ? dto.getAvance() : 0.0);
        entity.setMontantNet(dto.getMontantNet() != null ? dto.getMontantNet() : 0.0);
        entity.setObservations(dto.getObservations());
        if (dto.getType() != null) entity.setType(Facture.TypeFacture.valueOf(dto.getType()));
        if (dto.getStatut() != null) entity.setStatut(Facture.StatutFacture.valueOf(dto.getStatut()));
        if (dto.getProjetId() != null) {
            entity.setProjet(projetRepository.findById(dto.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + dto.getProjetId())));
        }
        if (dto.getAttachementId() != null) {
            attachementRepository.findById(dto.getAttachementId()).ifPresent(entity::setAttachement);
        }
    }

    private FactureDTO toDTO(Facture e) {
        FactureDTO dto = new FactureDTO();
        dto.setId(e.getId());
        dto.setNumero(e.getNumero());
        dto.setType(e.getType() != null ? e.getType().name() : null);
        dto.setDateEmission(e.getDateEmission());
        dto.setDateEcheance(e.getDateEcheance());
        dto.setMontantHT(e.getMontantHT());
        dto.setTva(e.getTva());
        dto.setMontantTTC(e.getMontantTTC());
        dto.setRetenuGarantie(e.getRetenuGarantie());
        dto.setAvance(e.getAvance());
        dto.setMontantNet(e.getMontantNet());
        dto.setStatut(e.getStatut() != null ? e.getStatut().name() : null);
        dto.setObservations(e.getObservations());
        if (e.getProjet() != null) { dto.setProjetId(e.getProjet().getId()); dto.setProjetNom(e.getProjet().getNom()); }
        if (e.getAttachement() != null) { dto.setAttachementId(e.getAttachement().getId()); dto.setAttachementNumero(e.getAttachement().getNumero()); }
        if (e.getCreatedBy() != null) { dto.setCreatedById(e.getCreatedBy().getId()); dto.setCreatedByUsername(e.getCreatedBy().getUsername()); }
        Double totalPaye = paiementRepository.sumMontantByFactureId(e.getId());
        dto.setTotalPaye(totalPaye);
        dto.setResteAPayer(e.getMontantNet() - totalPaye);
        return dto;
    }

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
