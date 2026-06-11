package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorDisco;
import com.example.api_builds.model.Disco;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discos")
@Tag(name = "Gerenciamento de Discos", description = "Operações para controle dos conjuntos de Drive Discs")
public class DiscoController {

    private final GerenciadorDisco gerenciador;

    public DiscoController(GerenciadorDisco gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping
    @Operation(summary = "Inserir Disco", description = "Cadastra um novo conjunto de disco.")
    public Disco criar(
            @RequestParam String nome,
            @RequestParam String urlImagem) {

        Disco disco = Disco.builder()
                .nome(nome)
                .urlImagem(urlImagem)
                .build();

        return gerenciador.inserir(disco);
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar Disco", description = "Modifica o nome ou imagem de um set de disco existente.")
    public Disco atualizar(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam String urlImagem) {

        Disco disco = Disco.builder()
                .nome(nome)
                .urlImagem(urlImagem)
                .build();

        return gerenciador.atualizar(id, disco);
    }

    @DeleteMapping("/deletar/{id}")
    @Operation(summary = "Deletar Disco", description = "Remove um conjunto de discos do sistema permanentemente.")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletar(id);
    }

    @GetMapping
    @Operation(summary = "Listar Discos", description = "Lista todos os conjuntos de discos disponíveis.")
    public List<Disco> listarTodos() {
        return gerenciador.listarTodos();
    }
}