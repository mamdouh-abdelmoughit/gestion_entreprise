package com.btp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
}