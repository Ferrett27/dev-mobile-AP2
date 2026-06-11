package com.example.api_builds.controller;

import com.example.api_builds.gerenciador.GerenciadorUsuario;
import com.example.api_builds.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Gerenciamento de Usuários", description = "Operações para controle de contas.")
public class UsuarioController {

    private final GerenciadorUsuario gerenciador;

    public UsuarioController(GerenciadorUsuario gerenciador) {
        this.gerenciador = gerenciador;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar Conta", description = "Cadastra um novo usuário.")
    public Usuario criar(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha) {

        Usuario usuario = Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .build();
        return gerenciador.inserir(usuario);
    }

    @GetMapping
    @Operation(summary = "Listar Usuários", description = "Lista todos os usuários.")
    public List<Usuario> listarTodos() {
        return gerenciador.listarTodos();
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer Login", description = "Valida as credenciais e retorna os dados do usuário logado.")
    public org.springframework.http.ResponseEntity<Usuario> login(@RequestBody Usuario loginData) {

        Usuario usuarioLogado = gerenciador.fazerLogin(loginData.getEmail(), loginData.getSenha());

        if (usuarioLogado != null) {
            return org.springframework.http.ResponseEntity.ok(usuarioLogado);
        } else {
            return org.springframework.http.ResponseEntity.status(401).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Conta de Usuário", description = "Remove permanentemente o usuário informado e todas as suas respectivas builds e inventários cadastrados.")
    public void deletar(@PathVariable Long id) {
        gerenciador.deletar(id);
    }
}