package com.raphaelcollin.contatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /* Conexão com o Banco de Dados */

        // Altere o USER e PASS de acordo com as configurações do Banco a ser acessado

public class ConnectionFactory {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3307/contatos?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    // Obtendo Conexão

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Erro: " + e.getMessage());
            return null;
        }
    }

    // Fechando a Conexão

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
