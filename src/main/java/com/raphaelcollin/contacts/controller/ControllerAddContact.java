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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerAddContact extends Controller implements Initializable {

    @FXML
    private GridPane root;
    @FXML
    private Button iconContactButton;
    @FXML
    private Button backButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button addButton;

    private ControllerContactFields controllerContactFields;

    private ResourceBundle resources;

    private static final String CLASS_CLEAR_BUTTON = "orange-button";
    private static final String CLASS_ADD_BUTTON = "blue-button";
    private static final String CLASS_BACK_BUTTON = "back-button";
    private static final String LOCATION_CONTACT_FIELDS_VIEW = "/contact_fields.fxml";
    private static final String LOCATION_LOADING_VIEW = "/loading_contacts.fxml";

    private static final String BUNDLE_KEY_ALERT_INFORMATION_TITLE = "add_contact_alert_information_title";
    private static final String BUNDLE_KEY_ALERT_INFORMATION_HEADER_TEXT = "add_contact_alert_information_header_text";
    private static final String BUNDLE_KEY_ALERT_INFORMATION_CONTENT_TEXT = "add_contact_alert_information_content_text";
    private static final String BUNDLE_KEY_ALERT_ERROR_TITLE = "alert_error_title";
    private static final String BUNDLE_KEY_ALERT1_ERROR_HEADER_TEXT = "add_contact_alert_error1_header_text";
    private static final String BUNDLE_KEY_ALERT1_ERROR_CONTENT_TEXT = "add_contact_alert_error1_content_text";
    private static final String BUNDLE_KEY_ALERT2_ERROR_HEADER_TEXT = "add_contact_alert_error2_header_text";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resources = resourceBundle;

        double width = getRootWidth();
        double height = getRootHeight();

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        double xSpacing = width * 0.0416666;
        double ySpacing = height * 0.0938438;
        root.setPadding(new Insets(xSpacing,ySpacing,xSpacing,ySpacing));
        root.setHgap(width * 0.0208333);
        root.setVgap(xSpacing);

        double backButtonSize = width * 0.1;

        backButton.setMinSize(backButtonSize, backButtonSize);
        backButton.setPrefSize(backButtonSize, backButtonSize);
        backButton.setMaxSize(backButtonSize, backButtonSize);
        backButton.setGraphic(new FontIcon("far-arrow-alt-circle-left:48:white"));
        backButton.getStyleClass().add(CLASS_BACK_BUTTON);

        double iconContactButtonSize = 0.1458333;

        iconContactButton.setMinSize(iconContactButtonSize, iconContactButtonSize);
        iconContactButton.setPrefSize(iconContactButtonSize, iconContactButtonSize);
        iconContactButton.setMaxSize(iconContactButtonSize, iconContactButtonSize);
        iconContactButton.setGraphic(new FontIcon("fas-portrait:64:blue"));
        iconContactButton.setDisable(true);
        iconContactButton.setBackground(Background.EMPTY);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_CONTACT_FIELDS_VIEW), resourceBundle);
            Parent contactFields = loader.load();
            root.getChildren().add(contactFields);
            GridPane.setConstraints(contactFields, 0,1);
            controllerContactFields = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }

        double bottomButtonsWidth = width * 0.20833333;
        double bottomButtonFontSize = width * 0.0375;
        double bottomButtonHeight = height * 0.075075;

        clearButton.setPrefWidth(bottomButtonsWidth);
        clearButton.setFont(new Font(bottomButtonFontSize));
        clearButton.setPrefHeight(bottomButtonHeight);

        addButton.setPrefWidth(bottomButtonsWidth);
        addButton.setFont(new Font(bottomButtonFontSize));
        addButton.setPrefHeight(bottomButtonHeight);

        clearButton.getStyleClass().add(CLASS_CLEAR_BUTTON);
        addButton.getStyleClass().add(CLASS_ADD_BUTTON);

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        iconContactButton.setEffect(dropShadow);
    }


    @FXML
    public void handleAdd() {

        try {
            Contact contact = controllerContactFields.buildContactFromFields();
            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call() {
                    root.setCursor(Cursor.WAIT);
                    DAO<Contact> dao = DAOFactory.getContactDAO();
                    return dao.insert(contact);
                }
            };

            task.setOnSucceeded(event -> {
                root.setCursor(Cursor.DEFAULT);
                if (task.getValue() >= 0) {
                    showAlert(Alert.AlertType.INFORMATION, resources.getString(BUNDLE_KEY_ALERT_INFORMATION_TITLE),
                                                           resources.getString(BUNDLE_KEY_ALERT_INFORMATION_HEADER_TEXT),
                                                           resources.getString(BUNDLE_KEY_ALERT_INFORMATION_CONTENT_TEXT),
                                                           root);


                    contact.setIdContact(task.getValue());

                } else {
                    showAlert(Alert.AlertType.ERROR, resources.getString(BUNDLE_KEY_ALERT_ERROR_TITLE),
                                                     resources.getString(BUNDLE_KEY_ALERT1_ERROR_HEADER_TEXT),
                                                     resources.getString(BUNDLE_KEY_ALERT1_ERROR_CONTENT_TEXT), root);
                }
            });

            new Thread(task).start();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, resources.getString(BUNDLE_KEY_ALERT_ERROR_TITLE),
                                             resources.getString(BUNDLE_KEY_ALERT2_ERROR_HEADER_TEXT),
                    e.getMessage(), root);
        }

    }

    @FXML
    public void handleClear() {
        controllerContactFields.clear();
    }

    @FXML
    public void handleBack() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_LOADING_VIEW), resources);
            Parent loadingViewRoot = loader.load();

            ControllerLoading controllerLoading = loader.getController();
            EventHandler<ActionEvent> event = e -> controllerLoading.getContactsList(false);

            AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
            changeView(containerRoot, root, loadingViewRoot, FROM_LEFT, event);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }




}
