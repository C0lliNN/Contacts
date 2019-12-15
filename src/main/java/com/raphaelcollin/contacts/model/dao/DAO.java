/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import javafx.collections.ObservableList;

public interface DAO<T> {

    int insert(T data);
    T select(int id);
    ObservableList<T> selectAll();
    boolean update(int id, T data);
    boolean delete(int id);

}
