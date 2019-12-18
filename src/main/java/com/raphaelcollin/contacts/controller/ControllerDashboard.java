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
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerDashboard extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField textField;
    @FXML
    private Button addButton;
    @FXML
    private ListView<GridPane> listView;

    private ObservableList<GridPane> gridList;

    private ResourceBundle resource;

    private static final String LOCATION_ADD_CONTACT_VIEW = "/add_contact.fxml";
    private static final String LOCATION_EDIT_CONTACT_VIEW = "/details_contact.fxml";
    private static final String ID_BUTTON_ADD_CONTACT = "add-contact";
    private static final String CLASS_CONTACT_ITEM_NAME_LABEL = "contact-item-name-label";
    private static final String CLASS_CONTACT_ITEM_NUMBER_LABEL = "contact-item-number-label";

    private static final String BUNDLE_KEY_ALERT_CONFIRMATION_TITLE = "dashboard_alert_confirmation_title";
    private static final String BUNDLE_KEY_ALERT_CONFIRMATION_HEADER_TEXT = "dashboard_alert_confirmation_headerText";
    private static final String BUNDLE_KEY_ALERT_CONFIRMATION_CONTENT_TEXT = "dashboard_alert_confirmation_contentText";
    private static final String BUNDLE_KEY_ALERT_ERROR_TITLE = "alert_error_title";
    private static final String BUNDLE_KEY_ALERT_ERROR_CONTENT_TEXT = "dashboard_alert_error_content_text";



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resource = resourceBundle;

        double width = getRootWidth();
        double height = getRootHeight();

        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        root.setPadding(new Insets(height * 0.02815, 0.0, 0.0, 0.0));
        BorderPane.setMargin(root.getCenter(), new Insets(height * 0.02815, 0.0, 0.0, 0.0));

        AnchorPane.setLeftAnchor(textField, width * 0.1);
        AnchorPane.setRightAnchor(textField, width * 0.27791666);
        AnchorPane.setRightAnchor(addButton, width * 0.135416);
        AnchorPane.setTopAnchor(addButton, 0.0);

        DropShadow dropShadow = new DropShadow(1.8, Color.rgb(30,30,30,0.4));

        FontIcon plusIcon = new FontIcon("fas-plus:32:#fafafa");
        addButton.setGraphic(plusIcon);
        addButton.setContentDisplay(ContentDisplay.CENTER);
        addButton.setPadding(Insets.EMPTY);

        double buttonSize = width * 0.09375;

        addButton.setMinSize(buttonSize, buttonSize);
        addButton.setPrefSize(buttonSize, buttonSize);
        addButton.setMaxSize(buttonSize, buttonSize);
        addButton.setId(ID_BUTTON_ADD_CONTACT);
        addButton.setEffect(dropShadow);

        textField.setPrefSize(width * 0.708333, buttonSize);
        textField.setMinSize(width * 0.708333, buttonSize);
        textField.setMaxSize(width * 0.708333, buttonSize);
        textField.setFont(new Font("Arial", width * 0.041666));
        textField.setEffect(dropShadow);

        listView.setEffect(dropShadow);

    }

    @FXML
    public void handleAddContact() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_ADD_CONTACT_VIEW), resource);
            Parent addView = loader.load();

            AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
            changeView(containerRoot, root, addView, FROM_RIGHT, null);

        } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }

    }


    @FXML
    public void handleEditContact(){
        GridPane gridPane = listView.getSelectionModel().getSelectedItem();

        if (gridPane != null){

            int intId = Integer.parseInt(((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(0)).getText());

            String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();
            String sexo = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(2)).getText();
            String numero = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(3)).getText();
            String email = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(4)).getText();
            String descricao = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(5)).getText();

            Contact contact = new Contact(intId,nome,sexo,numero,email,descricao);

            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource(LOCATION_EDIT_CONTACT_VIEW), resource);
                Parent edidContactRoot = fxmlLoader.load();

                ControllerContactDetails controllerContactDetails = fxmlLoader.getController();
                controllerContactDetails.setOldContact(contact);
                controllerContactDetails.inicializarCampos();

                AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
                changeView(containerRoot, root, edidContactRoot, FROM_RIGHT, null);

            } catch (IOException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    public void handleDeleteContact(GridPane gridPane) {


        if (gridPane != null){

            int intId = Integer.parseInt(((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(0)).getText());
            String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();

            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, resource.getString(BUNDLE_KEY_ALERT_CONFIRMATION_TITLE),
                    resource.getString(BUNDLE_KEY_ALERT_CONFIRMATION_HEADER_TEXT) + " " + nome + "?",
                    resource.getString(BUNDLE_KEY_ALERT_CONFIRMATION_CONTENT_TEXT), root);


            if (result.isPresent() && result.get().equals(ButtonType.OK)){
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() {
                        DAO<Contact> dao = DAOFactory.getContactDAO();
                        return dao.delete(intId);
                    }
                };

                task.setOnSucceeded(event -> {

                    if (task.getValue()){
                        gridList.remove(gridPane);
                    } else {
                        showAlert(Alert.AlertType.ERROR,BUNDLE_KEY_ALERT_ERROR_TITLE,nome,
                                BUNDLE_KEY_ALERT_ERROR_CONTENT_TEXT, root);
                    }
                });

                new Thread(task).start();

            }
        }

    }

    public void setupContacts(ObservableList<GridPane> gridList) {

        for (int i = 0; i < gridList.size(); i++) {

            Node nameLabel = ((VBox) gridList.get(i).getChildren().get(1)).getChildren().get(1);
            nameLabel.getStyleClass().add(CLASS_CONTACT_ITEM_NAME_LABEL);

            Node numberLabel = ((VBox) gridList.get(i).getChildren().get(1)).getChildren().get(3);
            numberLabel.getStyleClass().add(CLASS_CONTACT_ITEM_NUMBER_LABEL);

            gridList.get(i).setOnMouseClicked(event -> handleEditContact());

            Button button = ((Button) ((HBox) gridList.get(i).getChildren().get(2)).getChildren().get(0));

            final int position = i;

            button.setOnAction(event -> handleDeleteContact(gridList.get(position)));
        }

        this.gridList = gridList;
        listView.setItems(gridList);
    }

}
