/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import com.raphaelcollin.contacts.model.Contact;
import com.raphaelcollin.contacts.model.dao.DAO;
import com.raphaelcollin.contacts.model.dao.DAOFactory;
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

    private static final String LOCATION_CONTACT_VIEW = "/contact_item.fxml";
    private static final String LOCATION_DASHBOARD_VIEW = "/dashboard.fxml";
    private Task<ObservableList<GridPane>> task;
    private ResourceBundle resources;

    private static final String BUNDLE_KEY_LABEL_TEXT = "loading_view_label_text";
    private static final String BUNDLE_KEY_ALERT_TITLE = "alert_error_title";
    private static final String BUNDLE_KEY_ALERT_CONTENT_TEXT = "loading_view_alert_content_text";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resources = resourceBundle;

        double width = getRootWidth();
        double height = getRootHeight();

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        root.setSpacing(height * 0.0375375);

        label.setFont(Font.font(width * 0.0625));

        double indicatorSize = width * 0.166666;

        indicator.setPrefSize(indicatorSize, indicatorSize);
        indicator.setMinSize(indicatorSize, indicatorSize);
        indicator.setMaxSize(indicatorSize, indicatorSize);


        task = new Task<>() {
            @Override
            protected ObservableList<GridPane> call() throws Exception {

                DAO<Contact> dao = DAOFactory.getContactDAO();

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

                return gridList;

            }
        };

    }

    public void getContactsList(boolean transition) {



        task.setOnSucceeded(e -> {

            indicator.setOpacity(0.0);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_DASHBOARD_VIEW), resources);
                Parent dashboardRoot = loader.load();
                ControllerDashboard dashboardController = loader.getController();

                ObservableList<GridPane> contactsList = task.getValue();

                if (contactsList == null) {
                    label.setText(resources.getString(BUNDLE_KEY_LABEL_TEXT));
                    indicator.setOpacity(0);

                    showAlert(Alert.AlertType.ERROR, resources.getString(BUNDLE_KEY_ALERT_TITLE), resources.getString(BUNDLE_KEY_LABEL_TEXT),
                            resources.getString(BUNDLE_KEY_ALERT_CONTENT_TEXT), root);

                    Platform.exit();

                } else {

                    AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
                    dashboardController.setupContacts(contactsList);

                    if (transition) {
                        changeView(containerRoot, root, dashboardRoot, FROM_RIGHT, null);
                    } else {
                        containerRoot.getChildren().setAll(dashboardRoot);
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });

        new Thread(task).start();
    }
}
