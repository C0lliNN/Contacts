/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import com.raphaelcollin.contacts.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContactDAO implements DAO<Contact>{

    private static final String TABLE_CONTACTS_DB = "contacts";
    private static final String COLUMN_IDCONTACT_DB = "idContact";
    private static final String COLUMN_NAME_DB = "name";
    private static final String COLUMN_GENDER_DB = "gender";
    private static final String COLUMN_PHONE_NUMBER_DB = "phoneNumber";
    private static final String COLUMN_EMAIL_DB = "email";
    private static final String COLUMN_DESCRIPTION_DB = "description";

    @Override
    public int insert(Contact contact) {

        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(TABLE_CONTACTS_DB);
        query.append(" (");
        query.append(COLUMN_NAME_DB).append(",");
        query.append(COLUMN_GENDER_DB).append(",");
        query.append(COLUMN_PHONE_NUMBER_DB).append(",");
        query.append(COLUMN_EMAIL_DB).append(",");
        query.append(COLUMN_DESCRIPTION_DB).append(") ");
        query.append("VALUES (?,?,?,?,?)");

        Connection connection = openConnection();

        try (PreparedStatement statement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getGender());
            statement.setString(3, contact.getPhoneNumber());
            statement.setString(4, contact.getEmail());
            statement.setString(5, contact.getDescription());

            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            int generatedId = resultSet.getInt(1);
            resultSet.close();

            return generatedId;

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            return -1;
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Contact select(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append(COLUMN_IDCONTACT_DB).append(",");
        query.append(COLUMN_NAME_DB).append(",");
        query.append(COLUMN_GENDER_DB).append(",");
        query.append(COLUMN_PHONE_NUMBER_DB).append(",");
        query.append(COLUMN_EMAIL_DB).append(",");
        query.append(COLUMN_DESCRIPTION_DB);
        query.append(" FROM ");
        query.append(TABLE_CONTACTS_DB);
        query.append(" WHERE ");
        query.append(COLUMN_IDCONTACT_DB);
        query.append(" = ?");

        Connection connection = openConnection();

        try (PreparedStatement statement = connection.prepareStatement(query.toString())){

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Contact contact = new Contact(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                    , resultSet.getString(4),resultSet.getString(5),resultSet.getString(5));

            resultSet.close();

            return contact;


        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public ObservableList<Contact> selectAll() {

        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append(COLUMN_IDCONTACT_DB).append(",");
        query.append(COLUMN_NAME_DB).append(",");
        query.append(COLUMN_GENDER_DB).append(",");
        query.append(COLUMN_PHONE_NUMBER_DB).append(",");
        query.append(COLUMN_EMAIL_DB).append(",");
        query.append(COLUMN_DESCRIPTION_DB);
        query.append(" FROM ");
        query.append(TABLE_CONTACTS_DB);
        query.append(" ORDER BY ");
        query.append(COLUMN_NAME_DB);

        Connection connection = openConnection();

        try (PreparedStatement statement = connection.prepareStatement(query.toString());
             ResultSet resultSet = statement.executeQuery()) {

            ObservableList<Contact> contacts = FXCollections.observableArrayList();

            while (resultSet.next()){
                Contact contact = new Contact(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                        ,resultSet.getString(4),resultSet.getString(5),resultSet.getString(6));
                contacts.add(contact);
            }

            return contacts;

        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(int id, Contact contact) {

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(TABLE_CONTACTS_DB);
        query.append(" SET ");
        query.append(COLUMN_NAME_DB).append(" = ?").append(", ");
        query.append(COLUMN_GENDER_DB).append(" = ?").append(", ");
        query.append(COLUMN_PHONE_NUMBER_DB).append(" = ?").append(", ");
        query.append(COLUMN_EMAIL_DB).append(" = ?").append(", ");
        query.append(COLUMN_DESCRIPTION_DB).append(" = ?");
        query.append(" WHERE ");
        query.append(COLUMN_IDCONTACT_DB).append(" = ?");

        Connection connection = openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {

            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getGender());
            preparedStatement.setString(3, contact.getPhoneNumber());
            preparedStatement.setString(4, contact.getEmail());
            preparedStatement.setString(5, contact.getDescription());
            preparedStatement.setInt(6, id);

            preparedStatement.execute();

            return true;
        } catch (Exception e){
            System.err.println("Erro: " + e.getMessage());
            return false;
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM " + TABLE_CONTACTS_DB + " WHERE " + COLUMN_IDCONTACT_DB + " = ?";

        Connection connection = openConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.execute();
            return true;
        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return false;
        } finally {
            closeConnection(connection);
        }
    }


    private Connection openConnection() {
        return ConnectionFactory.getConnection();
    }

    private void closeConnection(Connection connection){
        ConnectionFactory.closeConnection(connection);
    }
}
