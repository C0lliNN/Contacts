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
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class ControllerContactDetails {

    @FXML
    private GridPane root;
    @FXML
    private ImageView imageView;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label sexoLabel;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private HBox hBoxRadio;
    @FXML
    private RadioButton masculinoRadio;
    @FXML
    private RadioButton femininoRadio;
    @FXML
    private Label numeroLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label descricaoLabel;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea descricaoField;
    @FXML
    private Button voltarButton;
    @FXML
    private Button salvarButton;

        // Contato que está sendo visualizado

    private Contact contact;

    // Tamanho atual da tela

    private Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

    /* Constantes */

    private static final int MAX_TEXTAREA_LENGTH = 245; // Maximo de Caracteres para o campo descricao

    // Regex de validação do campo email
    private static final String EMAIL_REG_EXP = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    // Regex de validação do campo número
    private static final String NUM_REG_EXP = "\\d{8,12}";

    private static final String URL_IMAGEM_ICONE = "file:arquivos/person-icon.png";
    private static final String CLASSE_BOTAO_VOLTAR =  "botao-laranja";
    private static final String CLASSE_BOTAO_SALVAR =  "botao-azul";
    private static final String CLASSE_FIELD = "adicionar-field";
    private static final String CLASSE_BORDA_VERMELHA = "borda-vermelha";
    private static final String URL_JANELA_PRINCIPAL = "/com/raphaelcollin/contacts/view/dashboard.fxml";

    // Constantes relativas a estrutura do Banco de Dados

    private static final String TABELA_CONTATOS_DB = "contatos";
    private static final String COLUNA_IDCONTATO_DB = "idContato";
    private static final String COLUNA_NOME_DB = "nome";
    private static final String COLUNA_SEXO_DB = "sexo";
    private static final String COLUNA_NUMERO_DB = "numero";
    private static final String COLUNA_EMAIL_DB = "email";
    private static final String COLUNA_DESCRICAO_DB = "descricao";

    public void initialize(){

        // Padding, Gaps e Spacing

        root.setPadding(new Insets(tamanhoTela.getHeight() * 0.018518, tamanhoTela.getWidth() * 0.026041,
                tamanhoTela.getHeight() * 0.018518, tamanhoTela.getWidth() * 0.026041));
        root.setHgap(tamanhoTela.getWidth() * 0.0052083);
        root.setVgap(tamanhoTela.getHeight() * 0.018518);
        hBoxRadio.setSpacing(tamanhoTela.getWidth() * 0.0052083);

        // Alinhando e definindo tamanho de controles

        imageView.setImage(new Image(URL_IMAGEM_ICONE));
        imageView.setFitWidth(tamanhoTela.getWidth() * 0.06);
        imageView.setFitHeight(tamanhoTela.getWidth()* 0.06);

        voltarButton.setPrefWidth(tamanhoTela.getWidth() * 0.121875);
        voltarButton.setFont(new Font(tamanhoTela.getWidth() * 0.012));
        voltarButton.setPrefHeight(tamanhoTela.getHeight() * 0.03703);

        salvarButton.setPrefWidth(tamanhoTela.getWidth() * 0.121875);
        salvarButton.setFont(new Font(tamanhoTela.getWidth() * 0.012));
        salvarButton.setPrefHeight(tamanhoTela.getHeight() * 0.03703);

        // Definindo Tamanho das Fontes

        double tamanhoFonte = tamanhoTela.getWidth() * 0.01145;

        nomeLabel.setFont(new Font(tamanhoFonte));
        nomeField.setFont(new Font(tamanhoFonte));
        sexoLabel.setFont(new Font(tamanhoFonte));
        masculinoRadio.setFont(new Font(tamanhoFonte));
        femininoRadio.setFont(new Font(tamanhoFonte));
        numeroLabel.setFont(new Font(tamanhoFonte));
        numeroField.setFont(new Font(tamanhoFonte));
        emailLabel.setFont(new Font(tamanhoFonte));
        emailField.setFont(new Font(tamanhoFonte));
        descricaoLabel.setFont(new Font(tamanhoFonte));
        descricaoField.setFont(new Font(tamanhoFonte));

        // CSS

        voltarButton.getStyleClass().add(CLASSE_BOTAO_VOLTAR);
        salvarButton.getStyleClass().add(CLASSE_BOTAO_SALVAR);
        nomeField.getStyleClass().add(CLASSE_FIELD);
        numeroField.getStyleClass().add(CLASSE_FIELD);
        emailField.getStyleClass().add(CLASSE_FIELD);
        descricaoField.getStyleClass().add(CLASSE_FIELD);


        // Colocando Labels para os Campos

        nomeLabel.setLabelFor(nomeField);
        numeroLabel.setLabelFor(numeroField);
        emailLabel.setLabelFor(emailField);
        descricaoLabel.setLabelFor(descricaoField);
        descricaoLabel.setOnMouseClicked(event -> descricaoField.requestFocus());

        // Criando efeito de sombra externa que será usado nos controles abaixo

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        nomeField.setEffect(dropShadow);
        numeroField.setEffect(dropShadow);
        emailField.setEffect(dropShadow);
        descricaoField.setEffect(dropShadow);
        imageView.setEffect(dropShadow);

        /* Caso o texto de algum dos campos for alterado, o botão salvar será habilitando, indicando ao usuário que
           as mudanças precisam ser salvas. Nos campos número e email, quando o foco sair desses campos será verificado
           se eles são válidos usando suas respectivas regex. Se o campo for inválido será colocada uma
           borda vermelha, indicando para o usuário que o input é inválido */

        nomeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (salvarButton.isDisable()){
                salvarButton.setDisable(false);
            }
        });

        radioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (salvarButton.isDisable()){
                salvarButton.setDisable(false);
            }
        });

        numeroField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (salvarButton.isDisable()){
                salvarButton.setDisable(false);
            }
        });

        numeroField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                numeroField.getStyleClass().remove(CLASSE_BORDA_VERMELHA);
            }
            if (oldValue){
                if (!numeroField.getText().isEmpty() && !numeroField.getText().matches(NUM_REG_EXP)){
                    numeroField.getStyleClass().add(CLASSE_BORDA_VERMELHA);
                }
            }
        }));

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (salvarButton.isDisable()){
                salvarButton.setDisable(false);
            }
        });

        emailField.focusedProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue){
                emailField.getStyleClass().remove(CLASSE_BORDA_VERMELHA);
            }

            if (oldValue) {
                if(!emailField.getText().isEmpty() && !emailField.getText().matches(EMAIL_REG_EXP)){
                    emailField.getStyleClass().add(CLASSE_BORDA_VERMELHA);
                }
            }
        });

            // Controlando o máximo de caracteres do textArea descrição

        descricaoField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (salvarButton.isDisable()){
                salvarButton.setDisable(false);
            }
            if (descricaoField.getText().length() > MAX_TEXTAREA_LENGTH){
                descricaoField.setText(descricaoField.getText().substring(0,MAX_TEXTAREA_LENGTH));
                descricaoField.positionCaret(MAX_TEXTAREA_LENGTH);
            }
        });

    }

    /*
     * Este método será executado quando o Botão Salvar for acionado
     * Primeiramente, será verfificado se os campos não estão vazios e se possuem valores válidos.
     *
     * Se os campos não possuírem valores validos, será exibido um alert informando o erro
     * Se os campos forem válidos, será verificado quais campos foram alterados pelo usuarios para poder montar a query
     * corretamente.
     *
     * Se nenhum campos for alterado, será simplesmente desabilitado o botão salvar e não será feito mais nada
     *
     * Nesse processo podem acontecer erros como: usuário trocar o nome para um nome que já esteja cadastrado, etc...
     *
     * Se a query for executada com sucesso no banco, será exibido um alert informando o sucesso da atualização
     * Se não será exibida uma mensagem de erro
     * */


    @FXML
    public void handleSalvar(){

        String erro = null;
        boolean erroEncontrado = false;
        if (nomeField.getText().isEmpty() || numeroField.getText().isEmpty() || emailField.getText().isEmpty()){
            erro = "Existem campos Obrigatórios que não foram preenchidos!";
            erroEncontrado = true;
        }
        if (!erroEncontrado && !numeroField.getText().matches(NUM_REG_EXP)){
            erro = "O campo número não possui um valor válido!";
            erroEncontrado = true;
        }
        if (!erroEncontrado && !emailField.getText().matches(EMAIL_REG_EXP)){
            erro = "O campo email não possui um valor válido!";
            erroEncontrado = true;
        }

        if (!erroEncontrado) {

            if (!(nomeField.getText().equals(contact.getName()) && numeroField.getText().equals(contact.getPhoneNumber())
                    && emailField.getText().equals(contact.getEmail()) && ((contact.getDescription() != null && descricaoField.getText().isEmpty())
                    || (contact.getDescription() != null && descricaoField.getText().equals(contact.getDescription()))))) {

                Contact newContact = new Contact(nomeField.getText(), ((ToggleButton) radioGroup.getSelectedToggle()).getText(), numeroField.getText(),
                        emailField.getText(), descricaoField.getText());

                DAO<Contact> dao = new ContactDAO();

                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call(){
                        root.setCursor(Cursor.WAIT);
                        return dao.update(contact.getIdContact(), newContact);
                    }
                };

                task.setOnSucceeded(e -> {
                    root.setCursor(Cursor.DEFAULT);
                    if (task.getValue()) {
                        exibirAlert(Alert.AlertType.INFORMATION,"Processamento Realizado",
                                "Contato atualizado com sucesso","O contato foi atualizado e já " +
                                        "está disponível na lista de contatos!");

                        contact = newContact;
                        inicializarCampos();

                    } else {
                        exibirAlert(Alert.AlertType.ERROR,"Erro no Processamento",
                                "Não foi possível atualizar o contato","Verifique se os novos " +
                                        "campos já não foram adicionados a outros contatos");
                    }
                });

                // Executando acesso ao bando de dados em outra Thread

                new Thread(task).start();

            } else {
                salvarButton.setDisable(true);
            }

        } else { // Algum Campo está vazio

            exibirAlert(Alert.AlertType.ERROR,"Erro no processamento","Campo inválido",erro);

        }

        nomeField.requestFocus();
    }


        /* Este método será executado quando o botão voltar for acionado.
         * Quando isso acontecer, será carregada a janela principal da aplicação */

    @FXML
    public void handleVoltar() {
        Stage stage = (Stage) root.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(URL_JANELA_PRINCIPAL));
            stage.getScene().setRoot(root);
        } catch (IOException e){
            System.out.println("Erro: " + e.getMessage());
        }

    }

        // Quando um Label sofrer um click, será dado foco para o seu respectivo campo

    @FXML
    public void handleLabelClick(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        TextField textField = (TextField) label.getLabelFor();
        textField.requestFocus();
    }

        // Inicialização de Campos de acordo com o contato selecionado pelo usuário na Janela principal

    void inicializarCampos(){

        nomeField.setText(contact.getName());
        if (!contact.getGender().equalsIgnoreCase("Masculino")){
            femininoRadio.setSelected(true);
        }

        numeroField.setText(contact.getPhoneNumber());
        emailField.setText(contact.getEmail());
        if (contact.getDescription() != null){
            descricaoField.setText(contact.getDescription());
        }

        salvarButton.setDisable(true);
    }

    // Método auxiliar para exibir os alerts. Foi criado para evitar repetição de código

    private void exibirAlert(Alert.AlertType alertType, String title, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();

    }

    // Setter

    void setContact(Contact contact) {
        this.contact = contact;
    }
}
