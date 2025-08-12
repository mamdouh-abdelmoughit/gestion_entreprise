package com.btp.mapper;

import com.btp.dto.*;
import com.btp.entity.*;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    // ===== AffectationEmploye mappings =====
    public AffectationEmployeDTO toDTO(AffectationEmploye entity) {
        if (entity == null) return null;
        
        AffectationEmployeDTO dto = new AffectationEmployeDTO();
        dto.setId(entity.getId());
        dto.setEmployeId(entity.getEmploye() != null ? entity.getEmploye().getId() : null);
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        dto.setRole(entity.getRole());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setStatut(entity.getStatut());
        
        return dto;
    }

    public AffectationEmploye toEntity(AffectationEmployeDTO dto) {
        if (dto == null) return null;
        
        AffectationEmploye entity = new AffectationEmploye();
        entity.setId(dto.getId());
        entity.setRole(dto.getRole());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== AppelOffre mappings =====
    public AppelOffreDTO toDTO(AppelOffre entity) {
        if (entity == null) return null;
        
        AppelOffreDTO dto = new AppelOffreDTO();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setDescription(entity.getDescription());
        dto.setBudgetEstimatif(entity.getBudgetEstimatif());
        dto.setDatePublication(entity.getDatePublication());
        dto.setDateLimite(entity.getDateLimite());
        dto.setStatut(entity.getStatut());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        
        return dto;
    }

    public AppelOffre toEntity(AppelOffreDTO dto) {
        if (dto == null) return null;
        
        AppelOffre entity = new AppelOffre();
        entity.setId(dto.getId());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setBudgetEstimatif(dto.getBudgetEstimatif());
        entity.setDatePublication(dto.getDatePublication());
        entity.setDateLimite(dto.getDateLimite());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== Caution mappings =====
    public CautionDTO toDTO(Caution entity) {
        if (entity == null) return null;
        
        CautionDTO dto = new CautionDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setType(entity.getType());
        dto.setMontant(entity.getMontant());
        dto.setBanque(entity.getBanque());
        dto.setDateEmission(entity.getDateEmission());
        dto.setDateExpiration(entity.getDateExpiration());
        dto.setStatut(entity.getStatut());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        dto.setAppelOffreId(entity.getAppelOffre() != null ? entity.getAppelOffre().getId() : null);
        
        return dto;
    }

    public Caution toEntity(CautionDTO dto) {
        if (dto == null) return null;
        
        Caution entity = new Caution();
        entity.setId(dto.getId());
        entity.setNumero(dto.getNumero());
        entity.setType(dto.getType());
        entity.setMontant(dto.getMontant());
        entity.setBanque(dto.getBanque());
        entity.setDateEmission(dto.getDateEmission());
        entity.setDateExpiration(dto.getDateExpiration());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== Decompte mappings =====
    public DecompteDTO toDTO(Decompte entity) {
        if (entity == null) return null;
        
        DecompteDTO dto = new DecompteDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setDateDecompte(entity.getDateDecompte());
        dto.setPeriodeDebut(entity.getPeriodeDebut());
        dto.setPeriodeFin(entity.getPeriodeFin());
        dto.setMontantTotal(entity.getMontantTotal());
        dto.setMontantRegle(entity.getMontantRegle());
        dto.setMontantRestant(entity.getMontantRestant());
        dto.setStatut(entity.getStatut());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        
        return dto;
    }

    public Decompte toEntity(DecompteDTO dto) {
        if (dto == null) return null;
        
        Decompte entity = new Decompte();
        entity.setId(dto.getId());
        entity.setNumero(dto.getNumero());
        entity.setDateDecompte(dto.getDateDecompte());
        entity.setPeriodeDebut(dto.getPeriodeDebut());
        entity.setPeriodeFin(dto.getPeriodeFin());
        entity.setMontantTotal(dto.getMontantTotal());
        entity.setMontantRegle(dto.getMontantRegle());
        entity.setMontantRestant(dto.getMontantRestant());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== Depense mappings =====
    public DepenseDTO toDTO(Depense entity) {
        if (entity == null) return null;
        
        DepenseDTO dto = new DepenseDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setDescription(entity.getDescription());
        dto.setMontant(entity.getMontant());
        dto.setDateDepense(entity.getDateDepense());
        dto.setTypeDepense(entity.getTypeDepense());
        dto.setStatut(entity.getStatut());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        
        return dto;
    }

    public Depense toEntity(DepenseDTO dto) {
        if (dto == null) return null;
        
        Depense entity = new Depense();
        entity.setId(dto.getId());
        entity.setNumero(dto.getNumero());
        entity.setDescription(dto.getDescription());
        entity.setMontant(dto.getMontant());
        entity.setDateDepense(dto.getDateDepense());
        entity.setTypeDepense(dto.getTypeDepense());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== Document mappings =====
    public DocumentDTO toDTO(Document entity) {
        if (entity == null) return null;
        
        DocumentDTO dto = new DocumentDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setType(entity.getType());
        dto.setCheminFichier(entity.getCheminFichier());
        dto.setDateUpload(entity.getDateUpload());
        dto.setTaille(entity.getTaille());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        
        return dto;
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) return null;
        
        Document entity = new Document();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setType(dto.getType());
        entity.setCheminFichier(dto.getCheminFichier());
        entity.setDateUpload(dto.getDateUpload());
        entity.setTaille(dto.getTaille());
        
        return entity;
    }

    // ===== Employe mappings =====
    public EmployeDTO toDTO(Employe entity) {
        if (entity == null) return null;
        
        EmployeDTO dto = new EmployeDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setCin(entity.getCin());
        dto.setTelephone(entity.getTelephone());
        dto.setEmail(entity.getEmail());
        dto.setPoste(entity.getPoste());
        dto.setDateEmbauche(entity.getDateEmbauche());
        dto.setSalaire(entity.getSalaire());
        dto.setStatut(entity.getStatut());
        dto.setAdresse(entity.getAdresse());
        dto.setCompetences(entity.getCompetences());
        
        return dto;
    }

    public Employe toEntity(EmployeDTO dto) {
        if (dto == null) return null;
        
        Employe entity = new Employe();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setCin(dto.getCin());
        entity.setTelephone(dto.getTelephone());
        entity.setEmail(dto.getEmail());
        entity.setPoste(dto.getPoste());
        entity.setDateEmbauche(dto.getDateEmbauche());
        entity.setSalaire(dto.getSalaire());
        entity.setStatut(dto.getStatut());
        entity.setAdresse(dto.getAdresse());
        entity.setCompetences(dto.getCompetences());
        
        return entity;
    }

    // ===== Evenement mappings =====
    public EvenementDTO toDTO(Evenement entity) {
        if (entity == null) return null;
        
        EvenementDTO dto = new EvenementDTO();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setDescription(entity.getDescription());
        dto.setDateEvenement(entity.getDateEvenement());
        dto.setLieu(entity.getLieu());
        dto.setType(entity.getType());
        dto.setStatut(entity.getStatut());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        
        return dto;
    }

    public Evenement toEntity(EvenementDTO dto) {
        if (dto == null) return null;
        
        Evenement entity = new Evenement();
        entity.setId(dto.getId());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setDateEvenement(dto.getDateEvenement());
        entity.setLieu(dto.getLieu());
        entity.setType(dto.getType());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== Fournisseur mappings =====
    public FournisseurDTO toDTO(Fournisseur entity) {
        if (entity == null) return null;
        
        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setSociete(entity.getSociete());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setAdresse(entity.getAdresse());
        dto.setMatriculeFiscale(entity.getMatriculeFiscale());
        dto.setRegistreCommerce(entity.getRegistreCommerce());
        dto.setStatut(entity.getStatut());
        
        return dto;
    }

    public Fournisseur toEntity(FournisseurDTO dto) {
        if (dto == null) return null;
        
        Fournisseur entity = new Fournisseur();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setSociete(dto.getSociete());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());
        entity.setAdresse(dto.getAdresse());
        entity.setMatriculeFiscale(dto.getMatriculeFiscale());
        entity.setRegistreCommerce(dto.getRegistreCommerce());
        entity.setStatut(dto.getStatut());
        
        return entity;
    }

    // ===== Projet mappings =====
    public ProjetDTO toDTO(Projet entity) {
        if (entity == null) return null;
        
        ProjetDTO dto = new ProjetDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setDescription(entity.getDescription());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setBudget(entity.getBudget());
        dto.setStatut(entity.getStatut());
        dto.setAdresse(entity.getAdresse());
        
        return dto;
    }

    public Projet toEntity(ProjetDTO dto) {
        if (dto == null) return null;
        
        Projet entity = new Projet();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setDescription(dto.getDescription());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setBudget(dto.getBudget());
        entity.setStatut(dto.getStatut());
        entity.setAdresse(dto.getAdresse());
        
        return entity;
    }

    // ===== Role mappings =====
    public RoleDTO toDTO(Role entity) {
        if (entity == null) return null;
        
        RoleDTO dto = new RoleDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setDescription(entity.getDescription());
        
        return dto;
    }

    public Role toEntity(RoleDTO dto) {
        if (dto == null) return null;
        
        Role entity = new Role();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setDescription(dto.getDescription());
        
        return entity;
    }

    // ===== User mappings =====
    public UserDTO toDTO(User entity) {
        if (entity == null) return null;
        
