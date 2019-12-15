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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerAddContact extends Controller implements Initializable {


    @FXML
    private GridPane root;
    @FXML
    private ImageView imageView;
    @FXML
    private Button voltarButton;
    @FXML
    private Button adicionarButton;

    private ControllerContactFields fields;

    // Tamanho atual da tela

    private Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

    /* CONSTANTES */

    // Quantidade máxima de caracteres permitidos no textArea Descrição
    private static final int MAX_TEXTAREA_LENGTH = 245;

    // Regex de validação do campo email
    private static final String EMAIL_REG_EXP = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    // Regex de validação do campo número
    private static final String NUM_REG_EXP = "\\d{8,12}";

    private static final String URL_IMAGEM_ICONE = "file:arquivos/add-contact.png";
    private static final String CLASSE_BOTAO_VOLTAR = "botao-laranja";
    private static final String CLASSE_BOTAO_ADICIONAR = "botao-azul";
    private static final String CLASSE_FIELD = "adicionar-field";
    private static final String CLASSE_BORDA_VERMELHA = "borda-vermelha";
    private static final String URL_JANELA_PRINCIPAL = "/dashboard.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Padding, Gaps e Spacing

        root.setPadding(new Insets(tamanhoTela.getHeight() * 0.018518, tamanhoTela.getWidth() * 0.026041,
                tamanhoTela.getHeight() * 0.018518, tamanhoTela.getWidth() * 0.026041));
        root.setHgap(tamanhoTela.getWidth() * 0.0052083);
        root.setVgap(tamanhoTela.getHeight() * 0.018518);

        // Alinhando e definindo tamanho de controles

        imageView.setImage(new Image(URL_IMAGEM_ICONE));
        imageView.setFitWidth(tamanhoTela.getWidth() * 0.06);
        imageView.setFitHeight(tamanhoTela.getWidth() * 0.06);

        voltarButton.setPrefWidth(tamanhoTela.getWidth() * 0.121875);
        voltarButton.setFont(new Font(tamanhoTela.getWidth() * 0.012));
        voltarButton.setPrefHeight(tamanhoTela.getHeight() * 0.03703);

        adicionarButton.setPrefWidth(tamanhoTela.getWidth() * 0.121875);
        adicionarButton.setFont(new Font(tamanhoTela.getWidth() * 0.012));
        adicionarButton.setPrefHeight(tamanhoTela.getHeight() * 0.03703);

        // CSS

        voltarButton.getStyleClass().add(CLASSE_BOTAO_VOLTAR);
        adicionarButton.getStyleClass().add(CLASSE_BOTAO_ADICIONAR);

        // Criando efeito de sombra externa que será usado nos campos

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        imageView.setEffect(dropShadow);
    }

    /*
     * Primeiramente será verificado se existem campos obrigatórios que não foram preenchidos ou se existem campos
     * inválidos.
     * Se isso acontecer, será exibido um Alert informando o erro ao usuário.
     * Se não for encontrado erro, será executada a query no banco através do objeto contatoDAO que retornará true em caso
     * de sucesso e false em caso de erro. Se o contato for inserido com sucesso, será exibido um alert com a mensagem de sucesso,
     * Se tiver sido ocorrido um erro ao inserir o contato, tambem será exibido um alert com uma mensagem de erro
     * */

    @FXML
    public void handleAdicionar() {

        try {
            Contact contact = fields.buildContactFromFields();
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() {
                    root.setCursor(Cursor.WAIT);
                    DAO<Contact> dao = new ContactDAO();
                    return dao.insert(contact);
                }
            };

            task.setOnSucceeded(event -> {
                root.setCursor(Cursor.DEFAULT);
                if (task.getValue() >= 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Processamento Realizado", "Contato inserido com sucesso",
                            "O contato foi adicionado a lista de contatos com sucesso!", root);


                    contact.setIdContact(task.getValue());

                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro no Processamento", "Não foi possível inserir o contato",
                            "Verifique se os campos já não foram adicionados a outros contatos", root);
                }
            });

            new Thread(task).start();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro no Processamento", "Input inválido", e.getMessage(), root);
        }

    }

    /* Esse método será executado quando o botão voltar for acionado
     * Quando isso acontercer, será carregada a página principal da aplicação */

    @FXML
    public void handleVoltar() {
        Stage stage = (Stage) root.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(URL_JANELA_PRINCIPAL));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

    }


    public void setFields(ControllerContactFields fields) {
        this.fields = fields;
    }
}
