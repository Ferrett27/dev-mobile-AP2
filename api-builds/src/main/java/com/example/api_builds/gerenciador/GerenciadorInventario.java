package com.example.api_builds.gerenciador;

import com.example.api_builds.conexao.ConexaoBanco;
import com.example.api_builds.model.Inventario;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorInventario {

    public Inventario inserir(Inventario inv) {
        String query = "INSERT INTO inventarios (usuario_id, personagem_id, arma_id, disco4_id, disco2_id, status_disco4, status_disco5, status_disco6, total_substatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            comandoPreparado.setLong(1, inv.getUsuarioId());
            comandoPreparado.setLong(2, inv.getPersonagemId());
            
            comandoPreparado.setObject(3, inv.getArmaId(), Types.BIGINT);
            comandoPreparado.setObject(4, inv.getDisco4Id(), Types.BIGINT);
            comandoPreparado.setObject(5, inv.getDisco2Id(), Types.BIGINT);
            comandoPreparado.setString(6, inv.getStatusDisco4());
            comandoPreparado.setString(7, inv.getStatusDisco5());
            comandoPreparado.setString(8, inv.getStatusDisco6());
            comandoPreparado.setInt(9, inv.getTotalSubstatus());

            comandoPreparado.executeUpdate();

            ResultSet resultado = comandoPreparado.getGeneratedKeys();
            if (resultado.next()) {
                inv.setId(resultado.getLong(1));
            }
            return inv;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir inventário", e);
        }
    }

    public List<Inventario> listarPorUsuario(Long usuarioId) {
    List<Inventario> lista = new ArrayList<>();
    String query = "SELECT * FROM inventarios WHERE usuario_id = ?";

    try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
         PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

        comandoPreparado.setLong(1, usuarioId);
        ResultSet resultado = comandoPreparado.executeQuery();

        while (resultado.next()) {
            Object armaObj = resultado.getObject("arma_id");
            Object disco4Obj = resultado.getObject("disco4_id");
            Object disco2Obj = resultado.getObject("disco2_id");

            lista.add(Inventario.builder()
                    .id(resultado.getLong("id"))
                    .usuarioId(resultado.getLong("usuario_id"))
                    .personagemId(resultado.getLong("personagem_id"))
                    .armaId(armaObj != null ? ((Number) armaObj).longValue() : null)
                    .disco4Id(disco4Obj != null ? ((Number) disco4Obj).longValue() : null)
                    .disco2Id(disco2Obj != null ? ((Number) disco2Obj).longValue() : null)
                    .statusDisco4(resultado.getString("status_disco4"))
                    .statusDisco5(resultado.getString("status_disco5"))
                    .statusDisco6(resultado.getString("status_disco6"))
                    .totalSubstatus(resultado.getInt("total_substatus"))
                    .build());
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar inventário", e);
    }
    return lista;
}

    public Inventario atualizar(Long id, Inventario inv) {
        String query = "UPDATE inventarios SET arma_id = ?, disco4_id = ?, disco2_id = ?, status_disco4 = ?, status_disco5 = ?, status_disco6 = ?, total_substatus = ? WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {

            comandoPreparado.setObject(1, inv.getArmaId(), Types.BIGINT);
            comandoPreparado.setObject(2, inv.getDisco4Id(), Types.BIGINT);
            comandoPreparado.setObject(3, inv.getDisco2Id(), Types.BIGINT);
            comandoPreparado.setString(4, inv.getStatusDisco4());
            comandoPreparado.setString(5, inv.getStatusDisco5());
            comandoPreparado.setString(6, inv.getStatusDisco6());
            comandoPreparado.setInt(7, inv.getTotalSubstatus());
            comandoPreparado.setLong(8, id);

            comandoPreparado.executeUpdate();
            inv.setId(id);
            return inv;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar inventário", e);
        }
    }

    public void deletar(Long id) {
        String query = "DELETE FROM inventarios WHERE id = ?";
        try (Connection conexao = ConexaoBanco.getInstancia().getConexao();
             PreparedStatement comandoPreparado = conexao.prepareStatement(query)) {
            comandoPreparado.setLong(1, id);
            comandoPreparado.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar inventário", e);
        }
    }
}
