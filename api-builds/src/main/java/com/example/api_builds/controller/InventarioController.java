package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorInventario;
import com.example.api_builds.model.Inventario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@Tag(name = "Gerenciamento de Inventário", description = "Operações para os personagens cadastrados do usuário")
public class InventarioController {

    private final GerenciadorInventario gerenciador;

    public InventarioController(GerenciadorInventario gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping
    @Operation(summary = "Cadastrar Equipamentos", description = "Adiciona um personagem com seus equipamentos atuais no inventário do usuário.")
    public Inventario criar(
            @RequestParam Long armaId,
            @RequestParam Long disco4Id,
            @RequestParam Long disco2Id,
            @RequestParam String statusDisco4,
            @RequestParam String statusDisco5,
            @RequestParam String statusDisco6,
            @RequestParam Integer totalSubstatus) {

        Inventario inventario = Inventario.builder()
                .armaId(armaId)
                .disco4Id(disco4Id)
                .disco2Id(disco2Id)
                .statusDisco4(statusDisco4)
                .statusDisco5(statusDisco5)
                .statusDisco6(statusDisco6)
                .totalSubstatus(totalSubstatus)
                .build();

        return gerenciador.inserir(inventario);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar Inventário do Usuário", description = "Lista todos os personagens cadastrados de um usuário específico.")
    public List<Inventario> listarPorUsuario(@RequestParam Long usuarioId) {
        return gerenciador.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Equipamentos", description = "Modifica os equipamentos e sub-status de um personagem no inventário.")
    public Inventario atualizar(
            @PathVariable Long id,
            @RequestParam Long armaId,
            @RequestParam Long disco4Id,
            @RequestParam Long disco2Id,
            @RequestParam String statusDisco4,
            @RequestParam String statusDisco5,
            @RequestParam String statusDisco6,
            @RequestParam Integer totalSubstatus) {

        Inventario inventario = Inventario.builder()
                .armaId(armaId)
                .disco4Id(disco4Id)
                .disco2Id(disco2Id)
                .statusDisco4(statusDisco4)
                .statusDisco5(statusDisco5)
                .statusDisco6(statusDisco6)
                .totalSubstatus(totalSubstatus)
                .build();

        return gerenciador.atualizar(id, inventario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover do Inventário", description = "Deleta um personagem cadastrado do inventário.")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletar(id);
    }
}