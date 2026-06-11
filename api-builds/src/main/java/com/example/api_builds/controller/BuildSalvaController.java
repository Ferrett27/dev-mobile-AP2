package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorBuildSalva;
import com.example.api_builds.model.Build;
import com.example.api_builds.model.BuildSalva;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/builds-salvas")
@Tag(name = "Builds Salvas (Favoritos)", description = "Operações para salvar builds de outros jogadores no perfil.")
public class BuildSalvaController {

    private final GerenciadorBuildSalva gerenciador;

    public BuildSalvaController(GerenciadorBuildSalva gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping("/{buildId}")
    @Operation(summary = "Salvar Build", description = "Adiciona uma build à lista de salvas do usuário.")
    public void salvar(@PathVariable Long usuarioId, @PathVariable Long buildId) {
        gerenciador.favoritarBuild(usuarioId, buildId);
    }

    @DeleteMapping("/{buildId}")
    @Operation(summary = "Remover Build Salva", description = "Remove uma build da lista de salvas do usuário.")
    public void remover(@PathVariable Long usuarioId, @PathVariable Long buildId) {
        gerenciador.removerFavorito(usuarioId, buildId);
    }

    @GetMapping
    @Operation(summary = "Listar Builds Salvas", description = "Lista todas as builds que o usuário salvou no seu perfil.")
    public List<Build> listar(@RequestParam Long usuarioId) {
        return gerenciador.listarBuildsSalvas(usuarioId);
    }
}