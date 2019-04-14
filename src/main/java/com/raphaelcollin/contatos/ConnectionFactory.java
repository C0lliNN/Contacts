package com.raphaelcollin.contatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /* Conexao com o Banco de Dados */

        // Altere o USER e PASS de acordo com o seu Banco

public class ConnectionFactory {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/contatos?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    // Obtendo Conexao

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erro na Conexao", e);
        }

    }

    // Fechando a conexao

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

}
