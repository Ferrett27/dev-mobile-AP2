package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Build;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorBuild {

    public Build inserir(Build build) {
        String querySql = "INSERT INTO builds (usuario_id, personagem_id, arma_id, disco4_id, disco2_id, status_disco4, status_disco5, status_disco6) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {

            comandoPreparado.setLong(1, build.getUsuarioId());
            comandoPreparado.setLong(2, build.getPersonagemId());
            comandoPreparado.setLong(3, build.getArmaId());
            comandoPreparado.setLong(4, build.getDisco4Id());
            comandoPreparado.setLong(5, build.getDisco2Id());
            comandoPreparado.setString(6, build.getStatusDisco4());
            comandoPreparado.setString(7, build.getStatusDisco5());
            comandoPreparado.setString(8, build.getStatusDisco6());

            comandoPreparado.executeUpdate();
            
            ResultSet resultado = comandoPreparado.getGeneratedKeys();
            if (resultado.next()) {
                build.setId(resultado.getLong(1));
            }

            return build;

        } catch (SQLException erroSql) {
            throw new RuntimeException("Erro ao inserir build no banco de dados", erroSql);
        }
    }

    public List<Build> listarTodos() {
        List<Build> listaDeBuilds = new ArrayList<>();
        String querySql = "SELECT * FROM builds";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             Statement comandoSimples = conexao.createStatement();
             ResultSet resultado = comandoSimples.executeQuery(querySql)) {

            while (resultado.next()) {
                listaDeBuilds.add(Build.builder()
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

        } catch (SQLException erroSql) {
            throw new RuntimeException("Erro ao listar as builds do banco de dados", erroSql);
        }

        return listaDeBuilds;
    }

    public Build atualizar(Long id, Build build) {
        String querySql = "UPDATE builds SET arma_id = ?, disco4_id = ?, disco2_id = ?, status_disco4 = ?, status_disco5 = ?, status_disco6 = ? WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(querySql)) {

            comandoPreparado.setLong(1, build.getArmaId());
            comandoPreparado.setLong(2, build.getDisco4Id());
            comandoPreparado.setLong(3, build.getDisco2Id());
            comandoPreparado.setString(4, build.getStatusDisco4());
            comandoPreparado.setString(5, build.getStatusDisco5());
            comandoPreparado.setString(6, build.getStatusDisco6());
            comandoPreparado.setLong(7, id);

            comandoPreparado.executeUpdate();
            build.setId(id);
            return build;

        } catch (SQLException erroSql) {
            throw new RuntimeException("Erro ao atualizar build no banco de dados", erroSql);
        }
    }

    public void deletar(Long id) {
        String querySql = "DELETE FROM builds WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(querySql)) {

            comandoPreparado.setLong(1, id);
            comandoPreparado.executeUpdate();

        } catch (SQLException erroSql) {
            throw new RuntimeException("Erro ao deletar build no banco de dados", erroSql);
        }
    }
}
