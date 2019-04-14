package com.raphaelcollin.contatos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.awt.*;

public class ControllerContato {
    @FXML
    private ImageView imageView;
    @FXML
    private Label idField;
    @FXML
    private Label nomeField;
    @FXML
    private Label sexoField;
    @FXML
    private Label numeroField;
    @FXML
    private Label emailField;
    @FXML
    private Label descricaoField;


    public void initialize(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        nomeField.setFont(new Font(dimension.width * 0.012));
        numeroField.setFont(new Font(dimension.width * 0.012));

    }

    Label getIdField() {
        return idField;
    }

    Label getNomeField() {
        return nomeField;
    }

    Label getSexoField() {
        return sexoField;
    }

    Label getNumeroField() {
        return numeroField;
    }

    Label getEmailField() {
        return emailField;
    }

    Label getDescricaoField() {
        return descricaoField;
    }


    void setImage() {
        if (sexoField.getText().equalsIgnoreCase("Masculino")){
            imageView.setImage(new Image("file:arquivos/male-icon.png"));
        } else {
            imageView.setImage(new Image("file:arquivos/female-icon.png"));
        }
    }


}
