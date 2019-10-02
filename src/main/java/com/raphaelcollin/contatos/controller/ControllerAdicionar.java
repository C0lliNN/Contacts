package com.raphaelcollin.contatos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
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


public class ControllerAdicionar {


    @FXML
    private GridPane root;
    @FXML
    private ImageView imageView;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private JFXRadioButton masculinoRadio;
    @FXML
    private JFXRadioButton femininoRadio;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label sexoLabel;
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
    private HBox hBoxRadio;
    @FXML
    private JFXButton voltarButton;
    @FXML
    private JFXButton adicionarButton;

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
    private static final String CLASSE_BOTAO_VOLTAR =  "botao-laranja";
    private static final String CLASSE_BOTAO_ADICIONAR =  "botao-azul";
    private static final String CLASSE_FIELD = "adicionar-field";
    private static final String CLASSE_BORDA_VERMELHA = "borda-vermelha";
    private static final String URL_JANELA_PRINCIPAL = "/janela_principal.fxml";

    public void initialize(){

        // Padding, Gaps e Spacing

        root.setPadding(new Insets(tamanhoTela.getHeight() * 0.018518, tamanhoTela.getWidth() * 0.026041,
                tamanhoTela.getHeight() * 0.018518, tamanhoTela.getWidth() * 0.026041));
        root.setHgap(tamanhoTela.getWidth() * 0.0052083);
        root.setVgap(tamanhoTela.getHeight() * 0.018518);
        hBoxRadio.setSpacing(tamanhoTela.getWidth() * 0.0052083);

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
        nomeField.getStyleClass().add(CLASSE_FIELD);
        numeroField.getStyleClass().add(CLASSE_FIELD);
        emailField.getStyleClass().add(CLASSE_FIELD);
        descricaoField.getStyleClass().add(CLASSE_FIELD);

            // Colocando Labels para os seus respectivos campos

        nomeLabel.setLabelFor(nomeField);
        numeroLabel.setLabelFor(numeroField);
        emailLabel.setLabelFor(emailField);
        descricaoLabel.setLabelFor(descricaoField);
        descricaoLabel.setOnMouseClicked(event -> descricaoField.requestFocus());

            // Criando efeito de sombra externa que será usado nos campos

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        nomeField.setEffect(dropShadow);
        numeroField.setEffect(dropShadow);
        emailField.setEffect(dropShadow);
        descricaoField.setEffect(dropShadow);
        imageView.setEffect(dropShadow);


            // Controlando o máximo de caracters do textArea descrição

        descricaoField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (descricaoField.getText().length() > MAX_TEXTAREA_LENGTH){
                descricaoField.setText(descricaoField.getText().substring(0,MAX_TEXTAREA_LENGTH));
                descricaoField.positionCaret(MAX_TEXTAREA_LENGTH);
            }
        });

            /* Quando os campos Número e E-mail pederem o foco, será verificado se eles são válidos
               Se não forem, será colocada uma borda vermelha nesse campo, mostrando para o usuário que algo está errado */

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
                    root.setCursor(Cursor.WAIT);
                    return ContatoDAO.getInstance().adicionarContato(contato);
                }
            };

            task.setOnSucceeded(event -> {
                root.setCursor(Cursor.DEFAULT);
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

    /* Esse método será executado quando o botão voltar for acionado
     * Quando isso acontercer, será carregada a página principal da aplicação */

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

    // Quando um Label for clicado, será dado foco para o seu respectivo campo

    @FXML
    public void handleLabelClick(MouseEvent mouseEvent) {
        Label label = (Label) mouseEvent.getSource();
        TextField textField = (TextField) label.getLabelFor();
        textField.requestFocus();
    }

    // Método auxiliar para exibir os alerts. Foi criado para evitar repetição de código

    private void exibirAlert(Alert.AlertType alertType,String title, String cabecalho, String contentText){
        Alert alert = new Alert(alertType);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(cabecalho);
        alert.setContentText(contentText);
        alert.show();
    }

}
