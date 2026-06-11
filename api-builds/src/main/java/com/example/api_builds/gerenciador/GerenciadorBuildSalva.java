package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Build;
import com.example.api_builds.model.BuildSalva;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorBuildSalva {
    
    public void favoritarBuild(Long usuarioId, Long buildId) {
        String query = "INSERT INTO builds_salvas (usuario_id, build_id) VALUES (?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setLong(1, usuarioId);
            comandoPreparado.setLong(2, buildId);
            comandoPreparado.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            // Caso o usuario tente salvar a mesma build
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao favoritar build", e);
        }
    }
    
    public void removerFavorito(Long usuarioId, Long buildId) {
        String query = "DELETE FROM builds_salvas WHERE usuario_id = ? AND build_id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setLong(1, usuarioId);
            comandoPreparado.setLong(2, buildId);
            comandoPreparado.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover build dos favoritos", e);
        }
    }
    
    public List<Build> listarBuildsSalvas(Long usuarioId) {
        List<Build> lista = new ArrayList<>();
        
        String query = "SELECT b.* FROM builds b " +
                "INNER JOIN builds_salvas bs ON b.id = bs.build_id " +
                "WHERE bs.usuario_id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setLong(1, usuarioId);
            ResultSet resultado = comandoPreparado.executeQuery();

            while (resultado.next()) {
                lista.add(Build.builder()
                        .id(resultado.getLong("id"))
                        .usuarioId(resultado.getLong("usuario_id"))
                        .personagemId(resultado.getLong("personagem_id"))
                        .armaId(resultado.getLong("arma_id"))
                        .disco4Id(resultado.getLong("disco4_id"))
                        .disco2Id(resultado.getLong("disco2_id"))
                        .statusDisco4(resultado.getString("status_disco4"))
                        .statusDisco5(resultado.getString("status_disco5"))
                        .statusDisco6(resultado.getString("status_disco6"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar builds salvas", e);
        }
        return lista;
    }
}