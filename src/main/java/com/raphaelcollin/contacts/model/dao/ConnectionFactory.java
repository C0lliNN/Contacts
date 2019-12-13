/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /* Conexão com o Banco de Dados */

        // Altere o USER e PASS de acordo com as configurações do Banco a ser acessado

public class ConnectionFactory {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String PORT = "3306";
    private static final String DB_NAME = "contacts";
    private static final String URL = "jdbc:mysql://localhost:" + PORT + "/" + DB_NAME + "?useTimezone=true&serverTimezone=UTC&useSSL=false";
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
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

}
