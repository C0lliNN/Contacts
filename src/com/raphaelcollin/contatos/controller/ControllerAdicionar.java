package com.raphaelcollin.contatos.controller;

import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerAdicionar {

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView imageView;
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

    private static final int MAX_TEXTAREA_LENGTH = 245;

    public void initialize(){

            // Imagem

        imageView.setImage(new Image(getClass().
                getResourceAsStream("/com/raphaelcollin/contatos/imagens/banner.png")));

            // Colocando Labels para os seus respectivos campos

        labelNome.setLabelFor(nomeField);
        labelNumero.setLabelFor(numeroField);
        labelEmail.setLabelFor(emailField);
        labelDescricao.setLabelFor(descricaoField);
        labelDescricao.setOnMouseClicked( event -> descricaoField.requestFocus());

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
           if (oldValue && !numeroField.getText().trim().isEmpty()){
               if (!numeroField.getText().matches("\\d+")){
                   numeroField.getStyleClass().add("borda-vermelha");
               }
           }
        }));

        emailField.focusedProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue){
                emailField.getStyleClass().remove("borda-vermelha");
            }

            if (oldValue) {
                if(!emailField.getText().isEmpty() && !validateEmail()){
                    emailField.getStyleClass().add("borda-vermelha");
                }
            }
        });
    }

    /*
    * Primeiramente vamos verificar se existem campos obrigatorios que nao foram preenchidos ou se existem campos
    * invalidos.
    * Se isso acontecer, vamos exibir um Alert informando o erro ao usuario e finalizar o metodo
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

        if (!erroEncontrado && !numeroField.getText().matches("\\d+")){
            erro = "O campo número não possui um valor válido!";
            erroEncontrado = true;
        }
        if (!erroEncontrado && !validateEmail()){
            erro = "O campo email não possui um valor válido!";
            erroEncontrado = true;
        }


        if (!erroEncontrado) {
            String nome = nomeField.getText();
            String numero = numeroField.getText();
            String email = emailField.getText();
            String descricao;
            if (descricaoField.getText().isEmpty()){
                descricao = null;
            } else {
                descricao = descricaoField.getText();
            }

            Contato contato = new Contato(nome,numero,email,descricao);

            ContatoDAO contatoDAO = new ContatoDAO();
            boolean result = contatoDAO.insertContato(contato);

            if (result) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(gridPane.getScene().getWindow());
                alert.setTitle("Processamento Realizado");
                alert.setHeaderText("Contato inserido com sucesso");
                alert.setContentText("O contato foi adicionado a lista de contatos com sucesso!");
                alert.show();
                nomeField.clear();
                numeroField.clear();
                emailField.clear();
                descricaoField.clear();
                nomeField.requestFocus();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(gridPane.getScene().getWindow());
                alert.setTitle("Erro no Processamento");
                alert.setHeaderText("Não foi possível inserir o contato");
                alert.setContentText("Verifique se os campos já não foram adicionados a outros contatos");
                alert.show();
            }

        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(gridPane.getScene().getWindow());
            alert.setTitle("Erro no processamento");
            alert.setHeaderText("Campo inválido");
            alert.setContentText(erro);
            alert.show();
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/raphaelcollin/contatos/view/janela_principal.fxml"));
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

    // Validacao do campo email que deve conter caracteres especiais como @, . etc

    private boolean validateEmail(){
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern .matcher(emailField.getText());
        return matcher.find();
    }

}
