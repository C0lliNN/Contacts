/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import com.raphaelcollin.contacts.model.Contact;
import com.raphaelcollin.contacts.model.dao.ContactDAO;
import com.raphaelcollin.contacts.model.dao.DAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLoading extends Controller implements Initializable {
    @FXML
    private VBox root;
    @FXML
    private Label label;
    @FXML
    private ProgressIndicator indicator;

    private ResourceBundle bundle;

    private static final String LOCATION_CONTACT_VIEW = "/contact_item.fxml";
    private static final String LOCATION_DASHBOARD_VIEW = "/dashboard.fxml";
    private Task<ObservableList<GridPane>> task;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        root.setPrefSize(getRootWidth(), getRootHeight());
        root.setMinSize(getRootWidth(), getRootHeight());
        root.setMaxSize(getRootWidth(), getRootHeight());

        root.setSpacing(20);

        label.setFont(Font.font(30));
        indicator.setPrefSize(80, 80);
        indicator.setMinSize(80, 80);
        indicator.setMaxSize(80, 80);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_DASHBOARD_VIEW), bundle);
            Parent dashboardRoot = loader.load();
            ControllerDashboard dashboardController = loader.getController();

            task = new Task<>() {
                @Override
                protected ObservableList<GridPane> call() throws Exception {

                    DAO<Contact> dao = new ContactDAO();

                    ObservableList<Contact> contacts = dao.selectAll();

                    if (contacts == null) {
                        return null;
                    }

                    ObservableList<GridPane> gridList = FXCollections.observableArrayList();

                    for (Contact contact : contacts) {

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOCATION_CONTACT_VIEW));
                        GridPane gridPane = fxmlLoader.load();

                        ControllerContactItem controllerContactItem = fxmlLoader.getController();
                        controllerContactItem.setupControls(contact);

                        gridList.add(gridPane);

                    }

                    Thread.sleep(1000);;

                    return gridList;

                }
            };

            task.setOnSucceeded(e -> {

                ObservableList<GridPane> contactsList = task.getValue();

                if (contactsList == null) {
                    label.setText("Erro no Banco de Dados");
                    indicator.setOpacity(0);

                    showAlert(Alert.AlertType.ERROR, "Contatos", "Erro no Banco de Dados",
                            "Não foi possível se conectar ao Banco de Dados MYSQL", root);

                    Platform.exit();

                } else {

                    AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
                    dashboardController.setupContacts(contactsList);

                    changeView(containerRoot, root, dashboardRoot, FROM_RIGHT);

                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getContactsList() {
        new Thread(task).start();
    }

}
