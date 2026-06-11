package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorPersonagem;
import com.example.api_builds.model.Personagem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personagens")
@Tag(name = "Gerenciamento de Personagens", description = "Operações para controle dos agentes do jogo")
public class PersonagemController {

    private final GerenciadorPersonagem gerenciador;

    public PersonagemController(GerenciadorPersonagem gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping
    @Operation(summary = "Inserir Personagem", description = "Cadastra um novo agente no sistema.")
    public Personagem criar(
            @RequestParam String nome,
            @RequestParam String urlImagem) {


        return gerenciador.inserir(Personagem.builder().nome(nome).urlImagem(urlImagem).build());
    }

    @GetMapping
    @Operation(summary = "Listar Personagens", description = "Lista todos os agentes disponíveis.")
    public List<Personagem> listarTodos() {
        return gerenciador.listarTodos();
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar Personagem", description = "Altera o nome ou a URL da imagem de um personagem existente.")
    public Personagem atualizar(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam String urlImagem) {

        Personagem personagem = Personagem.builder()
                .nome(nome)
                .urlImagem(urlImagem)
                .build();

        return gerenciador.atualizar(id, personagem);
    }

    @DeleteMapping("/deletar/{id}")
    @Operation(summary = "Deletar Personagem", description = "Remove um personagem permanentemente do sistema.")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletar(id);
    }

}