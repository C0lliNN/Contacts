/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

public class DAOFactory {

    public static ContactDAO getContactDAO() {
        return new ContactDAO();
    }
}
