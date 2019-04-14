package com.raphaelcollin.contatos.controller;

import com.jfoenix.controls.JFXButton;
import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;


public class ControllerAdicionar {


    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView imageView;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelNumero;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelDescricao;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea descricaoField;
    @FXML
    private JFXButton voltarButton;
    @FXML
    private JFXButton adicionarButton;

    // Quantidade máxima de caracteres permitidos no textArea Descrição

    private static final int MAX_TEXTAREA_LENGTH = 245;

    // Regex de validação do campo email

    private static final String EMAIL_REG_EXP = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    // Regex de validação do campo número

    private static final String NUM_REG_EXP = "\\d{8,12}";

    public void initialize(){

            // Imagem

        imageView.setImage(new Image("file:arquivos/add-contact.png"));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        imageView.setFitWidth(dimension.width * 0.06);
        imageView.setFitHeight(dimension.width * 0.06);

            // Colocando Labels para os seus respectivos campos

        labelNome.setLabelFor(nomeField);
        labelNumero.setLabelFor(numeroField);
        labelEmail.setLabelFor(emailField);
        labelDescricao.setLabelFor(descricaoField);
        labelDescricao.setOnMouseClicked( event -> descricaoField.requestFocus());

            // Criando efeito de sombra externa que será usado nos campos

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        nomeField.setEffect(dropShadow);
        numeroField.setEffect(dropShadow);
        emailField.setEffect(dropShadow);
        descricaoField.setEffect(dropShadow);


            // Controlando o maximo de caracters do text Area descicao

        descricaoField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (descricaoField.getText().length() > MAX_TEXTAREA_LENGTH){
                descricaoField.setText(descricaoField.getText().substring(0,MAX_TEXTAREA_LENGTH));
                descricaoField.positionCaret(MAX_TEXTAREA_LENGTH);
            }
        });

            // Quando os campos Numero e E-mail pederem o foco, vamos verficar se eles sao validos
            // Se nao forem vamos colocar uma borda vermelha nesse campo, mostrando para o usuario que algo esta errado

        numeroField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
           if (newValue) {
               numeroField.getStyleClass().remove("borda-vermelha");
           }
           if (oldValue){
               if (!numeroField.getText().isEmpty() && !numeroField.getText().matches(NUM_REG_EXP)){
                   numeroField.getStyleClass().add("borda-vermelha");
               }
           }
        }));

        emailField.focusedProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue){
                emailField.getStyleClass().remove("borda-vermelha");
            }

            if (oldValue) {
                if(!emailField.getText().isEmpty() && !emailField.getText().matches(EMAIL_REG_EXP)){
                    emailField.getStyleClass().add("borda-vermelha");
                }
            }
        });

        // Configurando as dimensões e o tamanho da fonte dos botões adicionar e voltar

        voltarButton.setPrefWidth(dimension.width * 0.3 / 2 - 54);
        voltarButton.setFont(new javafx.scene.text.Font(dimension.width * 0.012));
        voltarButton.setPrefHeight(40);
        adicionarButton.setPrefWidth(dimension.width * 0.3 / 2 - 54);
        adicionarButton.setFont(new Font(dimension.width * 0.012));
        adicionarButton.setPrefHeight(40);
    }

    /*
    * Primeiramente vamos verificar se existem campos obrigatorios que nao foram preenchidos ou se existem campos
    * invalidos.
    * Se isso acontecer, vamos exibir um Alert informando o erro ao usuario.
    * Se nao for encontrado erro, vamos executar o query no banco atraves do objeto contatoDAO que retornara true em caso
    * de sucesso e false em caso de erro. Se o contato for inserido com sucesso, vamos exibir um alert com a mensagem de sucesso,
    * Se tiver sido ocorrido um erro ao inserir o contato, tambem vamos exibir um alert com uma mensagem de erro
    * */

    @FXML
    public void handleAdicionar(){
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
            String nome = nomeField.getText();
            String sexo = ((RadioButton) radioGroup.getSelectedToggle()).getText();
            String numero = numeroField.getText();
            String email = emailField.getText();
            String descricao;
            if (descricaoField.getText().isEmpty()){
                descricao = null;
            } else {
                descricao = descricaoField.getText();
            }

            Contato contato = new Contato(nome,sexo,numero,email,descricao);

            Task<Integer> task = new Task<Integer>() {
                @Override
                protected Integer call(){
                    gridPane.setCursor(Cursor.WAIT);
                    return ContatoDAO.getInstance().insertContato(contato);
                }
            };

            task.setOnSucceeded(event -> {
                gridPane.setCursor(Cursor.DEFAULT);
                if (task.getValue() >= 0) {
                    exibirAlert(Alert.AlertType.INFORMATION,"Processamento Realizado","Contato inserido com sucesso",
                            "O contato foi adicionado a lista de contatos com sucesso!");

                    nomeField.clear();
                    numeroField.clear();
                    emailField.clear();
                    descricaoField.clear();
                    nomeField.requestFocus();

                    contato.setId(task.getValue());

                } else {
                    exibirAlert(Alert.AlertType.ERROR,"Erro no Processamento","Não foi possível inserir o contato",
                            "Verifique se os campos já não foram adicionados a outros contatos");
                }
            });

            new Thread(task).start();


        } else{
            exibirAlert(Alert.AlertType.ERROR,"Erro no processamento","Erro no processamento",erro);
        }
    }

    /* Esse metodo sera executado quando o botao voltar for acionado
     * Quando isso acontercer, vamos carregar a pagina principal da aplicaco
     * com a largura sendo igual a 30% da altura total da tela
     * e a altura sendo igual a 60% da altura total da tela*/

    @FXML
    public void handleVoltar() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/janela_principal.fxml"));
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            stage.setScene(new Scene(root,screenSize.width * 0.3,screenSize.height * 0.6));
        } catch (IOException e){
            System.out.println("Erro: " + e.getMessage());
        }

    }

    // Quando um Label for clicado, vamos dar foco para o seu respectivo campo

    @FXML
    public void handleLabelClick(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        TextField textField = (TextField) label.getLabelFor();
        textField.requestFocus();
    }

    @FXML
    public void handleMouseEntered() {
        gridPane.setCursor(Cursor.HAND);
    }

    // Quando o cursor do mouse sair de cima de algum botão, iremos voltar o cursor para o normal

    @FXML
    public void handleMouseExited() {
        gridPane.setCursor(Cursor.DEFAULT);
    }

    // Método auxiliar para exibir os alerts. Foi criado para evitar muita repetição de código

    private void exibirAlert(Alert.AlertType alertType,String title, String cabecalho, String contentText){
        Alert alert = new Alert(alertType);
        alert.initOwner(gridPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(cabecalho);
        alert.setContentText(contentText);
        alert.show();
    }

}
