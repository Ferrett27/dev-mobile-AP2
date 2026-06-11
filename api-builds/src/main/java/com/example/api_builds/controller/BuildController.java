package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorBuild;
import com.example.api_builds.model.Build;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/builds")
@Tag(name = "Gerenciamento de Builds", description = "Operações CRUD para as builds dos personagens")
public class BuildController {

    private final GerenciadorBuild gerenciador;

    public BuildController(GerenciadorBuild gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping
    @Operation(summary = "Inserir Build", description = "Adiciona uma nova build à base de dados.")
    public Build criar(
            @RequestParam Long usuarioId,
            @RequestParam Long personagemId,
            @RequestParam Long armaId,
            @RequestParam Long disco4Id,
            @RequestParam Long disco2Id,
            @RequestParam String status4,
            @RequestParam String status5,
            @RequestParam String status6) {

        Build build = Build.builder()
                .usuarioId(usuarioId)
                .personagemId(personagemId)
                .armaId(armaId)
                .disco4Id(disco4Id)
                .disco2Id(disco2Id)
                .statusDisco4(status4)
                .statusDisco5(status5)
                .statusDisco6(status6)
                .build();

        return gerenciador.inserir(build);
    }

    @GetMapping
    @Operation(summary = "Listar Build", description = "Lista todas as builds na base de dados.")
    public List<Build> listarTodas() {
        return gerenciador.listarTodos();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar Build", description = "Modifica uma build existente na base de dados via id.")
    public Build atualizar(
            @PathVariable Long id,
            @RequestParam Long armaId,
            @RequestParam Long disco4Id,
            @RequestParam Long disco2Id,
            @RequestParam String statusDisco4,
            @RequestParam String statusDisco5,
            @RequestParam String statusDisco6) {

        Build build = Build.builder()
                .armaId(armaId)
                .disco4Id(disco4Id)
                .disco2Id(disco2Id)
                .statusDisco4(statusDisco4)
                .statusDisco5(statusDisco5)
                .statusDisco6(statusDisco6)
                .build();

        return gerenciador.atualizar(id, build);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Build", description = "Deleta uma build na base de dados via id.")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletar(id);
    }
}
