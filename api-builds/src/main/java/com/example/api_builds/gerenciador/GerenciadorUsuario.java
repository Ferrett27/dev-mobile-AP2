package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Usuario;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorUsuario {

    public Usuario inserir(Usuario usuario) {
        String query = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            comandoPreparado.setString(1, usuario.getNome());
            comandoPreparado.setString(2, usuario.getEmail());
            comandoPreparado.setString(3, usuario.getSenha());
            comandoPreparado.executeUpdate();

            ResultSet resultado = comandoPreparado.getGeneratedKeys();
            if (resultado.next()) {
                usuario.setId(resultado.getLong(1));
            }
            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário", e);
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT id, nome, email FROM usuarios";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             Statement comandoPreparado = conexao.createStatement();
             ResultSet resultado = comandoPreparado.executeQuery(query)) {

            while (resultado.next()) {
                lista.add(Usuario.builder()
                        .id(resultado.getLong("id"))
                        .nome(resultado.getString("nome"))
                        .email(resultado.getString("email"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuários", e);
        }
        return lista;
    }

    public Usuario fazerLogin(String email, String senha) {
        String query = "SELECT id, nome, email FROM usuarios WHERE email = ? AND senha = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setString(1, email);
            comandoPreparado.setString(2, senha);

            ResultSet resultado = comandoPreparado.executeQuery();

            if (resultado.next()) {
                return Usuario.builder()
                        .id(resultado.getLong("id"))
                        .nome(resultado.getString("nome"))
                        .email(resultado.getString("email"))
                        .build();
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tentar fazer login", e);
        }
    }

    public void deletar(Long id) {
        String sqlBuilds = "DELETE FROM builds WHERE usuario_id = ?";
        String sqlInventarios = "DELETE FROM inventarios WHERE usuario_id = ?";
        String sqlUsuario = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao()) {
            conexao.setAutoCommit(false);

            try (PreparedStatement comandoPreparadoBuilds = conexao.prepareStatement(sqlBuilds);
                 PreparedStatement comandoPreparadoInventarios = conexao.prepareStatement(sqlInventarios);
                 PreparedStatement comandoPreparadoUsuario = conexao.prepareStatement(sqlUsuario)) {

                comandoPreparadoBuilds.setLong(1, id);
                comandoPreparadoBuilds.executeUpdate();

                comandoPreparadoInventarios.setLong(1, id);
                comandoPreparadoInventarios.executeUpdate();

                comandoPreparadoUsuario.setLong(1, id);
                comandoPreparadoUsuario.executeUpdate();

                conexao.commit();

            } catch (SQLException e) {
                conexao.rollback();
                throw new RuntimeException("Erro ao executar a remoção do usuário. Operação cancelada.", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro de conexão ao tentar deletar o usuário.", e);
        }
    }
}