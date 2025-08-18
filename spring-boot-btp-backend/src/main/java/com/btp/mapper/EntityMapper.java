package com.btp.mapper;

import com.btp.dto.*;
import com.btp.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        // FIX: Safely convert enum to string
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }

        return dto;
    }

    public AffectationEmploye toEntity(AffectationEmployeDTO dto) {
        if (dto == null) return null;

        AffectationEmploye entity = new AffectationEmploye();
        entity.setId(dto.getId());
        entity.setRole(dto.getRole());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        // FIX: Safely convert string to enum
        if (dto.getStatut() != null) {
            entity.setStatut(AffectationEmploye.StatutAffectation.valueOf(dto.getStatut()));
        }

        return entity;
    }

    // ===== AppelOffre mappings =====
    public AppelOffreDTO toDTO(AppelOffre entity) {
        if (entity == null) return null;

        AppelOffreDTO dto = new AppelOffreDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero()); // Map the new field
        dto.setTitre(entity.getIntitule()); // FIX: Field was named intitule in entity
        dto.setMaitreDOuvrage(entity.getMaitreDOuvrage());
        dto.setDescription(entity.getDescription());
        dto.setBudgetEstimatif(entity.getMontantEstime() != null ? java.math.BigDecimal.valueOf(entity.getMontantEstime()) : null); // FIX: Type conversion
        dto.setDatePublication(entity.getDatePublication());
        dto.setDateLimite(entity.getDateLimite()); // FIX: Field name mismatch
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);

        // FIX: Map the set of Fournisseur entities to a list of their IDs
        if (entity.getFournisseurs() != null) {
            List<Long> fournisseurIds = entity.getFournisseurs().stream()
                    .map(Fournisseur::getId)
                    .collect(Collectors.toList());
            dto.setFournisseurIds(fournisseurIds);
        }

        return dto;
    }

    public AppelOffre toEntity(AppelOffreDTO dto) {
        if (dto == null) return null;

        AppelOffre entity = new AppelOffre();
        entity.setId(dto.getId());
        entity.setNumero(dto.getNumero());
        entity.setIntitule(dto.getTitre()); // FIX: Field was named intitule in entity
        entity.setMaitreDOuvrage(dto.getMaitreDOuvrage()); // Map the new field
        entity.setDescription(dto.getDescription());
        entity.setMontantEstime(dto.getBudgetEstimatif() != null ? dto.getBudgetEstimatif().doubleValue() : null); // FIX: Type conversion
        entity.setDatePublication(dto.getDatePublication());
        entity.setDateLimite(dto.getDateLimite()); // FIX: Field name mismatch
        if (dto.getStatut() != null) {
            entity.setStatut(AppelOffre.StatutAppelOffre.valueOf(dto.getStatut().toUpperCase()));
        }

        return entity;
    }

    // ===== Caution mappings =====
    public CautionDTO toDTO(Caution entity) {
        if (entity == null) return null;

        CautionDTO dto = new CautionDTO();
        dto.setId(entity.getId());
        // There is no `numero` in DTO, but exists in entity. Assuming it's ok.
        dto.setType(entity.getType() != null ? entity.getType().name() : null); // FIX: Enum to String
        dto.setNumero(entity.getNumero()); // Map the new field
        dto.setBanque(entity.getBanque()); // Map the new field
        dto.setMontant(entity.getMontant() != null ? java.math.BigDecimal.valueOf(entity.getMontant()) : null); // FIX: Type conversion
        dto.setDateEmission(entity.getDateEmission());
        dto.setDateEcheance(entity.getDateExpiration()); // FIX: Field name mismatch
        // There is no `banque` in DTO.
        dto.setBeneficiaire(null); // No equivalent field in entity
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);
        dto.setAppelOffreId(entity.getAppelOffre() != null ? entity.getAppelOffre().getId() : null);

        // FIX: Map the new fournisseur relationship to its ID
        dto.setFournisseurId(entity.getFournisseur() != null ? entity.getFournisseur().getId() : null);

        return dto;
    }

    public Caution toEntity(CautionDTO dto) {
        if (dto == null) return null;

        Caution entity = new Caution();
        entity.setId(dto.getId());
        // No numero in DTO
        if (dto.getType() != null) {
            entity.setType(Caution.TypeCaution.valueOf(dto.getType()));
        }
        entity.setNumero(dto.getNumero()); // Map the new field
        entity.setMontant(dto.getMontant() != null ? dto.getMontant().doubleValue() : null); // FIX: Type conversion
        entity.setBanque(dto.getBanque()); // Map the new field
        entity.setDateEmission(dto.getDateEmission());
        entity.setDateExpiration(dto.getDateEcheance()); // FIX: Field name mismatch
        // No banque or beneficiaire in DTO
        if (dto.getStatut() != null) {
            entity.setStatut(Caution.StatutCaution.valueOf(dto.getStatut()));
        }

        return entity;
    }

    // ===== Decompte mappings =====
    public DecompteDTO toDTO(Decompte entity) {
        if (entity == null) return null;

        DecompteDTO dto = new DecompteDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setPeriode(entity.getPeriode()); // Map the new field
        dto.setMontantTotal(entity.getMontantTTC() != null ? java.math.BigDecimal.valueOf(entity.getMontantTTC()) : null); // Mapping TTC to total
        dto.setMontantPaye(null); // No direct mapping
        dto.setMontantRestant(null); // No direct mapping
        dto.setDateDecompte(entity.getDateDecompte());
        dto.setDescription(entity.getObservations());
        if(entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);

        return dto;
    }

    public Decompte toEntity(DecompteDTO dto) {
        if (dto == null) return null;

        Decompte entity = new Decompte();
        entity.setId(dto.getId());
        entity.setNumero(dto.getNumero());
        entity.setPeriode(dto.getPeriode()); // Map the new field
        entity.setMontantTTC(dto.getMontantTotal() != null ? dto.getMontantTotal().doubleValue() : null);
        // Set other financial fields to default values
        entity.setMontantHT(0.0);
        entity.setTva(0.0);
        entity.setRetenuGarantie(0.0);
        entity.setAvance(0.0);
        entity.setMontantNet(0.0);
        entity.setDateDecompte(dto.getDateDecompte());
        entity.setObservations(dto.getDescription());
        if(dto.getStatut() != null) {
            entity.setStatut(Decompte.StatutDecompte.valueOf(dto.getStatut()));
        }

        return entity;
    }

    // ===== Depense mappings =====
    public DepenseDTO toDTO(Depense entity) {
        if (entity == null) return null;

        DepenseDTO dto = new DepenseDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setMontant(entity.getMontant() != null ? java.math.BigDecimal.valueOf(entity.getMontant()) : null);
        dto.setDateDepense(entity.getDateDepense());
        if (entity.getCategorie() != null) {
            dto.setCategorie(entity.getCategorie().name());
        }
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);

        // FIX: Map the new employe relationship to its ID
        dto.setEmployeId(entity.getEmploye() != null ? entity.getEmploye().getId() : null);

        return dto;
    }

    public Depense toEntity(DepenseDTO dto) {
        if (dto == null) return null;

        Depense entity = new Depense();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setMontant(dto.getMontant() != null ? dto.getMontant().doubleValue() : null);
        entity.setDateDepense(dto.getDateDepense());
        if (dto.getCategorie() != null) {
            entity.setCategorie(Depense.CategorieDepense.valueOf(dto.getCategorie()));
        }
        if (dto.getStatut() != null) {
            entity.setStatut(Depense.StatutDepense.valueOf(dto.getStatut()));
        }

        return entity;
    }

    // ===== Document mappings =====
    public DocumentDTO toDTO(Document entity) {
        if (entity == null) return null;

        DocumentDTO dto = new DocumentDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        if (entity.getType() != null) {
            dto.setType(entity.getType().name());
        }
        dto.setChemin(entity.getFichier()); // FIX: Field name mismatch
        dto.setTaille(entity.getTailleFichier()); // FIX: Field name mismatch
        dto.setDateUpload(entity.getDateUpload());
        dto.setDescription(entity.getDescription());
        dto.setProjetId(entity.getProjet() != null ? entity.getProjet().getId() : null);

        // FIX: Map the new employe relationship to its ID
        dto.setEmployeId(entity.getEmploye() != null ? entity.getEmploye().getId() : null);

        return dto;
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) return null;

        Document entity = new Document();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        if (dto.getType() != null) {
            entity.setType(Document.TypeDocument.valueOf(dto.getType()));
        }
        entity.setFichier(dto.getChemin()); // FIX: Field name mismatch
        entity.setTailleFichier(dto.getTaille()); // FIX: Field name mismatch
        entity.setDateUpload(dto.getDateUpload());
        entity.setDescription(dto.getDescription());

        return entity;
    }

    // ===== Employe mappings =====
    public EmployeDTO toDTO(Employe entity) {
        if (entity == null) return null;

        EmployeDTO dto = new EmployeDTO();
        dto.setId(entity.getId());
        dto.setCin(entity.getCin()); // Map the new field
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setPoste(entity.getPoste());
        dto.setDateEmbauche(entity.getDateEmbauche() != null ? entity.getDateEmbauche() : null);
        dto.setSalaire(entity.getSalaire() != null ? java.math.BigDecimal.valueOf(entity.getSalaire()) : null);
        dto.setAdresse(entity.getAdresse());
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }

        return dto;
    }

    public Employe toEntity(EmployeDTO dto) {
        if (dto == null) return null;

        Employe entity = new Employe();
        entity.setId(dto.getId());
        entity.setCin(dto.getCin()); // Map the new field
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());
        entity.setPoste(dto.getPoste());
        entity.setDateEmbauche(dto.getDateEmbauche() != null ? LocalDate.from(dto.getDateEmbauche().atStartOfDay()) : null);
        entity.setSalaire(dto.getSalaire() != null ? dto.getSalaire().doubleValue() : null);
        entity.setAdresse(dto.getAdresse());
        if (dto.getStatut() != null) {
            entity.setStatut(Employe.StatutEmploye.valueOf(dto.getStatut()));
        }

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
        dto.setLieu(null); // No 'lieu' in entity
        if (entity.getType() != null) {
            dto.setType(entity.getType().name());
        }
        // No 'statut' in entity
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
        // No 'lieu' in entity
        if (dto.getType() != null) {
            entity.setType(Evenement.TypeEvenement.valueOf(dto.getType()));
        }
        // No 'statut' in entity

        return entity;
    }

    // ===== Fournisseur mappings =====
    public FournisseurDTO toDTO(Fournisseur entity) {
        if (entity == null) return null;

        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setContact(entity.getContact()); // Map the new field
        if (entity.getType() != null) { // Map the new enum field
            dto.setType(entity.getType().name());
        }
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setAdresse(entity.getAdresse());
        if (entity.getSpecialites() != null) {
            dto.setSpecialites(new java.util.HashSet<>(entity.getSpecialites()));
        }
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }

        return dto;
    }

    public Fournisseur toEntity(FournisseurDTO dto) {
        if (dto == null) return null;

        Fournisseur entity = new Fournisseur();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setEmail(dto.getEmail());
        if (dto.getType() != null) { // Map the new enum field
            entity.setType(Fournisseur.TypeFournisseur.valueOf(dto.getType().toUpperCase()));
        }
        entity.setContact(dto.getContact()); // Map the new field
        entity.setTelephone(dto.getTelephone());
        entity.setAdresse(dto.getAdresse());
        if (dto.getSpecialites() != null) {
            entity.setSpecialites(new java.util.ArrayList<>(dto.getSpecialites()));
        }
        if (dto.getStatut() != null) {
            entity.setStatut(Fournisseur.StatutFournisseur.valueOf(dto.getStatut()));
        }

        return entity;
    }

    // ===== Projet mappings =====
    // ... inside the EntityMapper class ...

    // Method to convert Entity -> DTO
    public ProjetDTO toDTO(Projet entity) {
        if (entity == null) return null;

        ProjetDTO dto = new ProjetDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setNom(entity.getNom());
        dto.setMaitreDOuvrage(entity.getMaitreDOuvrage());
        dto.setDescription(entity.getDescription());
        if (entity.getStatut() != null) {
            dto.setStatut(entity.getStatut().name());
        }
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFinPrevue());
        if (entity.getMontantContrat() != null) {
            dto.setMontantContrat(java.math.BigDecimal.valueOf(entity.getMontantContrat()));
        }
        dto.setAdresse(entity.getAdresseChantier());
        if (entity.getClient() != null) {
            dto.setClientId(entity.getClient().getId());
        }
        if (entity.getChefProjet() != null) {
            dto.setChefProjetId(entity.getChefProjet().getId());
        }
        return dto;
    }

    // Method to convert DTO -> Entity
    public Projet toEntity(ProjetDTO dto) {
        if (dto == null) return null;

        Projet entity = new Projet();
        entity.setId(dto.getId());
        entity.setNumero(dto.getNumero());
        entity.setNom(dto.getNom());
        entity.setMaitreDOuvrage(dto.getMaitreDOuvrage());
        entity.setDescription(dto.getDescription());
        if (dto.getStatut() != null) {
            entity.setStatut(Projet.StatutProjet.valueOf(dto.getStatut()));
        }
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFinPrevue(dto.getDateFin());
        if (dto.getMontantContrat() != null) {
            entity.setMontantContrat(dto.getMontantContrat().doubleValue());
        }
        entity.setAdresseChantier(dto.getAdresse());

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

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setTelephone(null); // No telephone in User entity
        dto.setEnabled(entity.getEnabled());
        dto.setLastLogin(null); // No lastLogin in User entity
        if(entity.getRoles() != null){
            dto.setRoles(entity.getRoles().stream().map(Role::getNom).collect(Collectors.toSet()));
        }

        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User entity = new User();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEnabled(dto.isEnabled());
        // Do not map password here for security reasons
        // Do not map roles here, as it's typically handled in the service

        return entity;
    }
    public ClientDTO toDTO(Client entity) {
        if (entity == null) return null;

        ClientDTO dto = new ClientDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setAdresse(entity.getAdresse());
        return dto;
    }

    public Client toEntity(ClientDTO dto) {
        if (dto == null) return null;

        Client entity = new Client();
        entity.setId(dto.getId());
        entity.setNom(dto.getNom());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());
        entity.setAdresse(dto.getAdresse());
        return entity;
    }

}