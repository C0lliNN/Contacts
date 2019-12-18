/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import com.raphaelcollin.contacts.model.Contact;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;


public class ControllerContactItem extends Controller{
    @FXML
    private GridPane root;
    @FXML
    private ImageView genderImageView;
    @FXML
    private Label idField;
    @FXML
    private Label nameField;
    @FXML
    private Label genderField;
    @FXML
    private Label numberField;
    @FXML
    private Label emailField;
    @FXML
    private Label descriptionField;
    @FXML
    private Button deleteButton;

    private static final String LOCATION_MALE_ICON = "/male-icon.png";
    private static final String LOCATION_FEMALE_ICON = "/female-icon.png";
    private static final String ID_DELETE_BUTTON = "delete-button";


    public void initialize(){

        double rootWidth = getRootWidth();
        double rootHeight = getRootHeight();

        root.setVgap(-rootHeight * 0.0375375);
        root.setHgap(rootWidth * 0.0208333);

        genderImageView.setFitHeight(rootWidth * 0.125);
        genderImageView.setFitWidth(rootWidth * 0.125);

        double fontSize = rootWidth * 0.0416666;
        double buttonSize = rootWidth * 0.072916;

        nameField.setFont(new Font(fontSize));
        numberField.setFont(new Font(fontSize));
        deleteButton.setPrefSize(buttonSize, buttonSize);
        deleteButton.setMinSize(buttonSize, buttonSize);
        deleteButton.setMaxSize(buttonSize,buttonSize);
        deleteButton.setGraphic(new FontIcon("far-trash-alt:24:white"));
        deleteButton.setId(ID_DELETE_BUTTON);
        HBox.setMargin(deleteButton, new Insets(0,fontSize,0,0));

    }

    void setupControls(Contact contact) {
        idField.setText(String.format("%d", contact.getIdContact()));
        nameField.setText(contact.getName());
        genderField.setText(contact.getGender());
        if (genderField.getText().equalsIgnoreCase("Male")){
            genderImageView.setImage(new Image(getClass().getResourceAsStream(LOCATION_MALE_ICON)));
        } else {
            genderImageView.setImage(new Image(getClass().getResourceAsStream(LOCATION_FEMALE_ICON)));
        }
        numberField.setText(contact.getPhoneNumber());
        emailField.setText(contact.getEmail());
        descriptionField.setText(contact.getDescription());
    }
}
