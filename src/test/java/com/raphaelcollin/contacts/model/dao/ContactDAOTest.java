/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import com.raphaelcollin.contacts.model.Contact;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactDAOTest {

    private ObservableList<Contact> testContacts;

    @BeforeEach
    void setup() {
        DAO<Contact> dao = new ContactDAO();
        testContacts = dao.selectAll();
    }

    @ParameterizedTest
    @CsvSource({"Teste 1, Male, 995977220,teste1@hotmail.com, Testing",
               "Teste 2, Female, 993212531, teste2@gmai.com, Testing",
                "Teste 3, Female, 995231252, teste3@yahoo.com, Testing"})
    @Order(1)
    void insert(ArgumentsAccessor accessor) {

        Contact contact = new Contact(accessor.getString(0), accessor.getString(1), accessor.getString(2),
                accessor.getString(3), accessor.getString(4));

        DAO<Contact> dao = new ContactDAO(contact);
        int result = dao.insert();
        Assertions.assertTrue(result >= 0);

    }

    @Test
    @Order(2)
    void selectAll() {

        Assertions.assertNotNull(testContacts);
        Assertions.assertFalse(testContacts.isEmpty());;

    }

    @Test
    @Order(3)
    void select() {

        Assertions.assertNotNull(testContacts);
        Assertions.assertFalse(testContacts.isEmpty());;

        for (Contact contact : testContacts) {
            DAO<Contact> dao = new ContactDAO(contact);
            Contact resultContact = dao.select();
            Assertions.assertNotNull(resultContact);
        }

    }


    @Test
    @Order(4)
    void update() {

        Assertions.assertNotNull(testContacts);
        Assertions.assertFalse(testContacts.isEmpty());;

        for (Contact contact : testContacts) {
            contact.setName(contact.getName() + "Updated");
            DAO<Contact> dao = new ContactDAO(contact);
            boolean result = dao.update();
            Assertions.assertTrue(result);
        }
    }

    @Test
    @Order(5)
    void delete() {

        Assertions.assertNotNull(testContacts);
        Assertions.assertFalse(testContacts.isEmpty());;

        for (Contact contact : testContacts) {
            DAO<Contact> dao = new ContactDAO(contact);
            boolean result = dao.delete();
            Assertions.assertTrue(result);
        }
    }
}