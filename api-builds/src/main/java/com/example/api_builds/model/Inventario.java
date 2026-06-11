package com.example.api_builds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    private Long id;
    private Long usuarioId;
    private Long personagemId;
    private Long armaId;
    private Long disco4Id;
    private Long disco2Id;
    private String statusDisco4;
    private String statusDisco5;
    private String statusDisco6;
    private Integer totalSubstatus;
}