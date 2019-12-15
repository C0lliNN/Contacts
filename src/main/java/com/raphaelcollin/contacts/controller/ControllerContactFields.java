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
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerContactFields implements Initializable {

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

    private static final String EMAIL_REG_EXP = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final String NUM_REG_EXP = "\\d{8,12}";
    private static final int MAX_TEXTAREA_LENGTH = 245;
    private static final String CLASS_FIELD = "adicionar-field";
    private static final String CLASS_RED_BORDER = "borda-vermelha";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle2D tamanhoTela = Screen.getPrimary().getVisualBounds();
        root.setVgap(tamanhoTela.getHeight() * 0.018518);
        hBoxRadio.setSpacing(tamanhoTela.getWidth() * 0.0052083);

        double tamanhoFonte = tamanhoTela.getWidth() * 0.01145;

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
        nameField.setText("");
        radioGroup.selectToggle(maleRadio);
        numberField.setText("");
        emailField.setText("");
        descriptionField.setText("");
    }

    public Contact buildContactFromFields() throws Exception {
        String erro = null;
        boolean erroEncontrado = false;
        if (nameField.getText().isEmpty() || numberField.getText().isEmpty() || emailField.getText().isEmpty()){
            erro = "Existem campos Obrigatórios que não foram preenchidos!";
            erroEncontrado = true;
        }

        if (!erroEncontrado && !numberField.getText().matches(NUM_REG_EXP)){
            erro = "O campo número não possui um valor válido!";
            erroEncontrado = true;
        }
        if (!erroEncontrado && !emailField.getText().matches(EMAIL_REG_EXP)){
            erro = "O campo email não possui um valor válido!";
            erroEncontrado = true;
        }

        if (!erroEncontrado) {
            String nome = nameField.getText();
            String sexo = ((RadioButton) radioGroup.getSelectedToggle()).getText();
            String numero = numberField.getText();
            String email = emailField.getText();
            String descricao;
            if (descriptionField.getText().isEmpty()) {
                descricao = null;
            } else {
                descricao = descriptionField.getText();
            }
            return new Contact(nome, sexo, numero, email, descricao);

        } else  {
            throw new Exception(erro);
        }
    }
}
