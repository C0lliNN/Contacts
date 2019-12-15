/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import com.raphaelcollin.contacts.model.Contact;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;

/* ListView Item */

public class ControllerContactItem {
    @FXML
    private GridPane root;
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

    private static final String URL_IMAGEM_ICONE_MASCULINO = "file:arquivos/male-icon.png";
    private static final String URL_IMAGEM_ICONE_FEMININO = "file:arquivos/female-icon.png";


    public void initialize(){
        Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

        root.setVgap(-tamanhoTela.getHeight() * 0.018518);
        root.setHgap(tamanhoTela.getWidth() * 0.010416);

        nomeField.setFont(new Font(tamanhoTela.getWidth()* 0.012));
        numeroField.setFont(new Font(tamanhoTela.getWidth() * 0.012));

    }

    void setupControls(Contact contact) {
        idField.setText(String.format("%d", contact.getIdContact()));
        nomeField.setText(contact.getName());
        sexoField.setText(contact.getGender());
        if (sexoField.getText().equalsIgnoreCase("Masculino")){
            imageView.setImage(new Image(URL_IMAGEM_ICONE_MASCULINO));
        } else {
            imageView.setImage(new Image(URL_IMAGEM_ICONE_FEMININO));
        }
        numeroField.setText(contact.getPhoneNumber());
        emailField.setText(contact.getEmail());
        descricaoField.setText(contact.getDescription());
    }


}
