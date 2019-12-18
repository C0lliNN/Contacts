/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import com.raphaelcollin.contacts.model.Contact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerContactFields extends Controller implements Initializable {

    @FXML
    private GridPane root;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private Label nameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private HBox hBoxRadio;

    private ResourceBundle resource;

    private static final String EMAIL_REG_EXP = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final String NUM_REG_EXP = "\\d{8,12}";
    private static final int MAX_TEXTAREA_LENGTH = 245;
    private static final String CLASS_FIELD = "adicionar-field";
    private static final String CLASS_RED_BORDER = "red-border";

    private static final String BUNDLE_KEY_ERROR_MESSAGE1 = "contact_fields_error_message1";
    private static final String BUNDLE_KEY_ERROR_MESSAGE2 = "contact_fields_error_message2";
    private static final String BUNDLE_KEY_ERROR_MESSAGE3 ="contact_fields_error_message3" ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resource = resourceBundle;

        double width = getRootWidth();
        double height = getRootHeight();

        root.setPrefWidth(width - 100);
        root.setMinWidth(width - 100);
        root.setMaxWidth(width - 100);

        root.setVgap(height * 0.0375375);
        root.setHgap(width * 0.0333333);
        hBoxRadio.setSpacing(width * 0.0208333);

        double tamanhoFonte = width * 0.0333333;

        nameLabel.setFont(new Font(tamanhoFonte));
        nameField.setFont(new Font(tamanhoFonte));
        genderLabel.setFont(new Font(tamanhoFonte));
        maleRadio.setFont(new Font(tamanhoFonte));
        femaleRadio.setFont(new Font(tamanhoFonte));
        numberLabel.setFont(new Font(tamanhoFonte));
        numberField.setFont(new Font(tamanhoFonte));
        emailLabel.setFont(new Font(tamanhoFonte));
        emailField.setFont(new Font(tamanhoFonte));
        descriptionLabel.setFont(new Font(tamanhoFonte));
        descriptionField.setFont(new Font(tamanhoFonte));

        nameField.getStyleClass().add(CLASS_FIELD);
        numberField.getStyleClass().add(CLASS_FIELD);
        emailField.getStyleClass().add(CLASS_FIELD);
        descriptionField.getStyleClass().add(CLASS_FIELD);


        nameLabel.setLabelFor(nameField);
        numberLabel.setLabelFor(numberField);
        emailLabel.setLabelFor(emailField);
        descriptionLabel.setLabelFor(descriptionField);
        descriptionLabel.setOnMouseClicked(event -> descriptionField.requestFocus());

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        nameField.setEffect(dropShadow);
        numberField.setEffect(dropShadow);
        emailField.setEffect(dropShadow);
        descriptionField.setEffect(dropShadow);

        descriptionField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (descriptionField.getText().length() > MAX_TEXTAREA_LENGTH){
                descriptionField.setText(descriptionField.getText().substring(0,MAX_TEXTAREA_LENGTH));
                descriptionField.positionCaret(MAX_TEXTAREA_LENGTH);
            }
        });

        numberField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                numberField.getStyleClass().remove(CLASS_RED_BORDER);
            }
            if (oldValue){
                if (!numberField.getText().isEmpty() && !numberField.getText().matches(NUM_REG_EXP)){
                    numberField.getStyleClass().add(CLASS_RED_BORDER);
                }
            }
        }));

        emailField.focusedProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue){
                emailField.getStyleClass().remove(CLASS_RED_BORDER);
            }

            if (oldValue) {
                if(!emailField.getText().isEmpty() && !emailField.getText().matches(EMAIL_REG_EXP)){
                    emailField.getStyleClass().add(CLASS_RED_BORDER);
                }
            }
        });
    }

    @FXML
    public void handleLabelClick(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        TextField textField = (TextField) label.getLabelFor();
        textField.requestFocus();
    }

    public void clear() {
        nameField.clear();
        radioGroup.selectToggle(maleRadio);
        numberField.clear();
        emailField.clear();
        descriptionField.clear();

        emailField.getStyleClass().remove(CLASS_RED_BORDER);
        numberField.getStyleClass().remove(CLASS_RED_BORDER);

        nameField.requestFocus();

    }

    public Contact buildContactFromFields() throws Exception {
        String errorMessage = null;
        boolean errorFounded = false;
        if (nameField.getText().isEmpty() || numberField.getText().isEmpty()){
            errorMessage = resource.getString(BUNDLE_KEY_ERROR_MESSAGE1);
            errorFounded = true;
        }

        if (!errorFounded && !numberField.getText().matches(NUM_REG_EXP)){
            errorMessage = resource.getString(BUNDLE_KEY_ERROR_MESSAGE2);
            errorFounded = true;
        }
        if (!errorFounded && !emailField.getText().isEmpty() && !emailField.getText().matches(EMAIL_REG_EXP)){
            errorMessage = resource.getString(BUNDLE_KEY_ERROR_MESSAGE3);
            errorFounded = true;
        }

        if (!errorFounded) {

            String name = nameField.getText();
            String gender = ((RadioButton) radioGroup.getSelectedToggle()).getText();
            String number = numberField.getText();
            String email = emailField.getText().isEmpty() ? null : emailField.getText();;
            String description = descriptionField.getText().isEmpty() ? null : descriptionField.getText();

            return new Contact(name, gender, number, email, description);

        } else  {
            throw new Exception(errorMessage);
        }
    }

    public void setFieldsText(Contact contact) {
        nameField.setText(contact.getName());
        numberField.setText(contact.getPhoneNumber());
        radioGroup.selectToggle(contact.getGender().equals("Male") ? maleRadio : femaleRadio);
        emailField.setText(contact.getEmail() == null ? "" : contact.getEmail());
        descriptionField.setText(contact.getDescription() == null ? "" : contact.getDescription());
        nameField.requestFocus();
    }
}
