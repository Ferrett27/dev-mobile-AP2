package com.example.api_builds.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static ConexaoBanco instancia;

    private final String jdbcUrl = "jdbc:mysql://uzzfghjcjt1w0wmy:VUcBvG0pkzg5YdhT67Le@bsyxgplyu70xbq3f2kvh-mysql.services.clever-cloud.com:3306/bsyxgplyu70xbq3f2kvh";
    private final String user = "uzzfghjcjt1w0wmy";
    private final String password = "VUcBvG0pkzg5YdhT67Le";

    private ConexaoBanco() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do MySQL não encontrado!", e);
        }
    }

    // Utilização do Singleton aqui
    public static ConexaoBanco getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBanco();
        }
        return instancia;
    }

    public Connection getConexao() throws SQLException {
        System.out.println("Tentando conectar com: " + user + " e senha: " + password);
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}
