package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Arma;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorArma {

    public Arma inserir(Arma arma) {
        String query = "INSERT INTO armas (nome, url_imagem) VALUES (?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            comandoPreparado.setString(1, arma.getNome());
            comandoPreparado.setString(2, arma.getUrlImagem());
            comandoPreparado.executeUpdate();

            ResultSet resultado = comandoPreparado.getGeneratedKeys();
            if (resultado.next()) {
                arma.setId(resultado.getLong(1));
            }
            return arma;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir arma", e);
        }
    }

    public List<Arma> listarTodos() {
        List<Arma> lista = new ArrayList<>();
        String query = "SELECT * FROM armas";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             Statement comandoPreparado = conexao.createStatement();
             ResultSet resultado = comandoPreparado.executeQuery(query)) {

            while (resultado.next()) {
                lista.add(Arma.builder()
                        .id(resultado.getLong("id"))
                        .nome(resultado.getString("nome"))
                        .urlImagem(resultado.getString("url_imagem"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar armas", e);
        }
        return lista;
    }

    public Arma atualizar(Long id, Arma arma) {
        String query = "UPDATE armas SET nome = ?, url_imagem = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conn.prepareStatement(query)) {

            comandoPreparado.setString(1, arma.getNome());
            comandoPreparado.setString(2, arma.getUrlImagem());
            comandoPreparado.setLong(3, id);

            comandoPreparado.executeUpdate();

            arma.setId(id);
            return arma;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar arma com ID: " + id, e);
        }
    }

    public void deletar(Long id) {
        String query = "DELETE FROM armas WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setLong(1, id);
            comandoPreparado.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar arma com ID: " + id, e);
        }
    }
}