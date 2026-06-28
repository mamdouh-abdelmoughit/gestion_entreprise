package com.btp.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AttachementDTO {
    private Long id;
    private String numero;
    private Long projetId;
    private String projetNom;
    private String periode;
    private LocalDate dateAttachement;
    private String description;
    private String statut;
    private String documentPdf;
    private List<LigneAttachementDTO> lignes;
    private Long createdById;
    private String createdByUsername;
}
