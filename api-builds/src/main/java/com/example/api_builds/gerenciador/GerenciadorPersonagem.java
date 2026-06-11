package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Personagem;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorPersonagem {

    public Personagem inserir(Personagem personagem) {
        String query = "INSERT INTO personagens (nome, url_imagem) VALUES (?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            comandoPreparado.setString(1, personagem.getNome());
            comandoPreparado.setString(2, personagem.getUrlImagem());
            comandoPreparado.executeUpdate();

            ResultSet rs = comandoPreparado.getGeneratedKeys();
            if (rs.next()) {
                personagem.setId(rs.getLong(1));
            }
            return personagem;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir personagem", e);
        }
    }

    public List<Personagem> listarTodos() {
        List<Personagem> lista = new ArrayList<>();
        String query = "SELECT * FROM personagens";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             Statement comandoPreparado = conexao.createStatement();
             ResultSet resultado = comandoPreparado.executeQuery(query)) {

            while (resultado.next()) {
                lista.add(Personagem.builder()
                        .id(resultado.getLong("id"))
                        .nome(resultado.getString("nome"))
                        .urlImagem(resultado.getString("url_imagem"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar personagens", e);
        }
        return lista;
    }
    
    public Personagem atualizar(Long id, Personagem personagem) {
        String query = "UPDATE personagens SET nome = ?, url_imagem = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conn.prepareStatement(query)) {

            comandoPreparado.setString(1, personagem.getNome());
            comandoPreparado.setString(2, personagem.getUrlImagem());
            comandoPreparado.setLong(3, id);

            comandoPreparado.executeUpdate();

            personagem.setId(id);
            return personagem;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar personagem com ID: " + id, e);
        }
    }
    
    public void deletar(Long id) {
        String query = "DELETE FROM personagens WHERE id = ?";

        try (Connection conn = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conn.prepareStatement(query)) {

            comandoPreparado.setLong(1, id);
            comandoPreparado.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar personagem com ID: " + id, e);
        }
    }
}