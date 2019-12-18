/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void testDBConnection() {
        Connection connection = DBConnection.getInstance().getConnection();
        Assertions.assertNotNull(connection);
        DBConnection.getInstance().closeConnection(connection);
        try {
            Assertions.assertTrue(connection.isClosed());
        } catch (SQLException e) {
            fail();
        }
    }

}