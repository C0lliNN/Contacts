/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model.dao;

import com.raphaelcollin.contacts.model.Contact;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactDAOTest {

    @ParameterizedTest
    @CsvSource({"Teste, Male, 995977220,teste1@hotmail.com, Testing",
            "Teste 2, Female, 993212531, teste2@gmai.com, Testing",
            "Teste 3, Female, 995231252, teste3@yahoo.com, Testing",
            "Teste 4, Male, 992231252, teste3@yahoo.com, Testing"})
    void testContactDAO(ArgumentsAccessor accessor) {
        DAO<Contact> dao = new ContactDAO();

        Contact contact = new Contact(accessor.getString(0), accessor.getString(1), accessor.getString(2),
                accessor.getString(3), accessor.getString(4));


        int generatedId = dao.insert(contact);
        Assertions.assertTrue(generatedId >= 0);

        Contact retrievedContact = dao.select(generatedId);
        Assertions.assertNotNull(retrievedContact);

        ObservableList<Contact> contacts = dao.selectAll();
        Assertions.assertNotNull(contacts);

        contact.setName(contact.getName() + " Updated");
        boolean result = dao.update(generatedId, contact);
        Assertions.assertTrue(result);

        result = dao.delete(generatedId);
        Assertions.assertTrue(result);
    }


}