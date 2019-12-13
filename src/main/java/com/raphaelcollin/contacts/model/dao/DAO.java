/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import javafx.collections.ObservableList;

import java.sql.Connection;

public abstract class DAO<T> {
    protected Connection connection;

    public abstract int insert();
    public abstract T select();
    public abstract ObservableList<T> selectAll();
    public abstract boolean update();
    public abstract boolean delete();

}
