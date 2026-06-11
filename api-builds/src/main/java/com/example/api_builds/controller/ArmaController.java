package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorArma;
import com.example.api_builds.model.Arma;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/armas")
@Tag(name = "Gerenciamento de Armas", description = "Operações para controle de W-Engines")
public class ArmaController {

    private final GerenciadorArma gerenciador;

    public ArmaController(GerenciadorArma gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping("/adicionar")
    @Operation(summary = "Inserir Arma", description = "Cadastra uma nova arma (W-Engine) usando parâmetros individuais.")
    public Arma criar(
            @RequestParam String nome,
            @RequestParam String urlImagem) {

        Arma arma = Arma.builder()
                .nome(nome)
                .urlImagem(urlImagem)
                .build();

        return gerenciador.inserir(arma);
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar Arma", description = "Altera o nome ou a URL da imagem de uma arma.")
    public Arma atualizar(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam String urlImagem) {

        Arma arma = Arma.builder()
                .nome(nome)
                .urlImagem(urlImagem)
                .build();

        return gerenciador.atualizar(id, arma);
    }

    @DeleteMapping("/deletar/{id}")
    @Operation(summary = "Deletar Arma", description = "Remove uma arma permanentemente do sistema.")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletar(id);
    }

    @GetMapping
    @Operation(summary = "Listar Armas", description = "Lista todas as armas disponíveis.")
    public List<Arma> listarTodas() {
        return gerenciador.listarTodos();
    }
}