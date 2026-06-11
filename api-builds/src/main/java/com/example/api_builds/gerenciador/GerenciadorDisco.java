package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Disco;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorDisco {

    public Disco inserir(Disco disco) {
        String query = "INSERT INTO discos (nome, url_imagem) VALUES (?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            comandoPreparado.setString(1, disco.getNome());
            comandoPreparado.setString(2, disco.getUrlImagem());
            comandoPreparado.executeUpdate();

            ResultSet resultado = comandoPreparado.getGeneratedKeys();
            if (resultado.next()) {
                disco.setId(resultado.getLong(1));
            }
            return disco;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir disco", e);
        }
    }

    public List<Disco> listarTodos() {
        List<Disco> lista = new ArrayList<>();
        String query = "SELECT * FROM discos";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             Statement comandoPreparado = conexao.createStatement();
             ResultSet resultado = comandoPreparado.executeQuery(query)) {

            while (resultado.next()) {
                lista.add(Disco.builder()
                        .id(resultado.getLong("id"))
                        .nome(resultado.getString("nome"))
                        .urlImagem(resultado.getString("url_imagem"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar discos", e);
        }
        return lista;
    }

    public Disco atualizar(long id, Disco disco) {
        String query = "UPDATE discos SET nome = ?, url_imagem = ? WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setString(1, disco.getNome());
            comandoPreparado.setString(2, disco.getUrlImagem());
            comandoPreparado.setLong(3, id);

            comandoPreparado.executeUpdate();
            disco.setId(id);
            return disco;

        } catch (SQLException erroSql) {
            throw new RuntimeException("Erro ao atualizar build no banco de dados", erroSql);
        }
    }

    public void deletar(Long id) {
        String query = "DELETE FROM discos WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setLong(1, id);
            comandoPreparado.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar disco com ID: " + id, e);
        }
    }
}