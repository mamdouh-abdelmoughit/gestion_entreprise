package com.btp.dto;

import lombok.Data;

@Data
public class LigneAttachementDTO {
    private Long id;
    private Long attachementId;
    private String designation;
    private String unite;
    private Double quantitePrevue;
    private Double quantiteRealisee;
    private Boolean alerte;
    private Long bpdeLigneId;
}
