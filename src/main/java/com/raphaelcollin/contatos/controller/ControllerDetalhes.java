package com.raphaelcollin.contatos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
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
import java.util.ArrayList;
import java.util.List;


public class ControllerDetalhes {

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Label labelNome;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private JFXRadioButton femininoRadio;
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
    private JFXButton salvarButton;

        // Contato que sera visto seus detalhes

    private Contato contato;

        // Maximo de Caracteres para o campo descricao

    private static final int MAX_TEXTAREA_LENGTH = 245;

    // Regex de validação do campo email

    private static final String EMAIL_REG_EXP = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    // Regex de validação do campo número

    private static final String NUM_REG_EXP = "\\d{8,12}";

    public void initialize(){

            // Imagem

        imageView.setImage(new Image("file:arquivos/person-icon.png"));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        imageView.setFitWidth(dimension.width * 0.06);
        imageView.setFitHeight(dimension.width * 0.06);

            // Colocando Labels para os Campos

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

        /* Caso o texto de algum dos campos for alterado, o botão salvar será habilitando, indicando ao usuário que
           as mudanças precisam ser salvas. Nos campos número e email, quando o foco sair desses campos iremos
           verificar se eles são válidos usando suas respectivas regex. Se o campo for inválido iremos colocar uma
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
                numeroField.getStyleClass().remove("borda-vermelha");
            }
            if (oldValue){
                if (!numeroField.getText().isEmpty() && !numeroField.getText().matches(NUM_REG_EXP)){
                    numeroField.getStyleClass().add("borda-vermelha");
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
                emailField.getStyleClass().remove("borda-vermelha");
            }

            if (oldValue) {
                if(!emailField.getText().isEmpty() && !emailField.getText().matches(EMAIL_REG_EXP)){
                    emailField.getStyleClass().add("borda-vermelha");
                }
            }
        });

            // Controlando o maximo de caracters do text Area descicao

        descricaoField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (salvarButton.isDisable()){
                salvarButton.setDisable(false);
            }
            if (descricaoField.getText().length() > MAX_TEXTAREA_LENGTH){
                descricaoField.setText(descricaoField.getText().substring(0,MAX_TEXTAREA_LENGTH));
                descricaoField.positionCaret(MAX_TEXTAREA_LENGTH);
            }
        });

            // Configurando as dimensões e o tamanho da fonte dos botões salvar e voltar

        voltarButton.setPrefWidth(dimension.width * 0.3 / 2 - 54);
        voltarButton.setFont(new javafx.scene.text.Font(dimension.width * 0.012));
        voltarButton.setPrefHeight(40);
        salvarButton.setPrefWidth(dimension.width * 0.3 / 2 - 54);
        salvarButton.setFont(new Font(dimension.width * 0.012));
        salvarButton.setPrefHeight(40);
    }

    /*
     * Esse metodo sera executado quando o Botao Salvar for acionado
     * Primeiramente, vamos verfificar se os campos nao estao vazios e se possuem valores validos.
     *
     * Se os campos nao possuirem valores validos, vamos exibir um alert informando o erro
     * Se os campos forem validos, vamos verificar quais campos foram alterados pelo usuarios para poder montar a query
     * corretamente.
     *
     * Se nenhum campos for alterado, vamos simplesmente desabilitar o botão salvar e não fazer mais nada
     *
     * Nesse processo podem acontecer erros como: usuario trocar o nome para um nome que ja esteja cadastrado, etc...
     *
     * Se a query for executada com sucesso no banco, vamos exibir um altert de confirmacao
     * Se não vamos exibir uma mensagem de erro
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

            // Objeto que será usado para construir a query

            StringBuilder sql = new StringBuilder("update contatos ");

            List<String> campos = new ArrayList<>();

            int count = 0;


                /* Se a condicao abaixo for verdadeira, significa que algum campo foi alterado, ou seja o contato
                    foi alterado, entao a atualizacao no banco precisa ser feita, se a condicao for falsa, ou seja,
                    nenhum campo foi alterada, vamos apenas desabilitar o botão salvar*/

            if (!(nomeField.getText().equals(contato.getNome()) && numeroField.getText().equals(contato.getNumero())
                    && emailField.getText().equals(contato.getEmail()) && ((contato.getDescricao() != null && descricaoField.getText().isEmpty())
                    || (contato.getDescricao() != null && descricaoField.getText().equals(contato.getDescricao()))))) {

                /* Estou usando o caracter especial ? ao invés de concatenar diretamente a string para eveitar ataques
                 * SQLInjection */

                if (!nomeField.getText().equals(contato.getNome())){
                    sql.append("set nome = ?");
                    campos.add(nomeField.getText());
                    count++;
                }

                if (!((RadioButton) radioGroup.getSelectedToggle()).getText().equals(contato.getSexo())){
                    if (count == 1) {
                        sql.append(", ");
                        sql.append("sexo = ?");
                    } else {
                        sql.append("set sexo = ?");
                    }

                    campos.add(((RadioButton) radioGroup.getSelectedToggle()).getText());
                    count++;
                }

                if (!numeroField.getText().equals(contato.getNumero())){
                    if (count >= 1) {
                        sql.append(", ");
                        sql.append("numero = ?");
                    } else {
                        sql.append("set numero = ?");
                    }

                    campos.add(numeroField.getText());
                    count++;
                }

                if (!emailField.getText().equals(contato.getEmail())){
                    if (count >= 1) {
                        sql.append(", ");
                        sql.append("email = ?");
                    } else {
                        sql.append("set email = ?");
                    }
                    campos.add(emailField.getText());
                    count++;
                }

                if ((contato.getDescricao() == null && !descricaoField.getText().trim().isEmpty()) || (contato.getDescricao() != null
                        && !descricaoField.getText().equals(contato.getDescricao()))) {
                    if (count >= 1) {
                        sql.append(", ");
                        sql.append("descricao = ?");
                    } else {
                        sql.append("set descricao = ?");
                    }

                    campos.add(descricaoField.getText());
                }

                sql.append(" where idContato = ?");

                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call(){
                        gridPane.setCursor(Cursor.WAIT);
                        return ContatoDAO.getInstance().updateContato(sql.toString(),campos,contato.getId());
                    }
                };

                task.setOnSucceeded(e -> {
                    gridPane.setCursor(Cursor.DEFAULT);
                    if (task.getValue()) {
                        exibirAlert(Alert.AlertType.INFORMATION,"Processamento Realizado",
                                "Contato atualizado com sucesso","O contato foi atualizado e já " +
                                        "está disponível na lista de contatos!");

                        contato = ContatoDAO.getInstance().selectContato(contato.getId());
                        inicializarCampos();

                    } else {
                        exibirAlert(Alert.AlertType.ERROR,"Erro no Processamento",
                                "Não foi possível atualizar o contato","Verifique se os novos " +
                                        "campos já não foram adicionados a outros contatos");
                    }
                });

                // Executando acesso ao bando de dados em outra query

                new Thread(task).start();

            } else {
                salvarButton.setDisable(true);
            }

        } else {

            exibirAlert(Alert.AlertType.ERROR,"Erro no processamento","Campo inválido",erro);

        }

        nomeField.requestFocus();
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

    // Quando o cursor mouse estiver sobre algum botão, iremos colocar trocar o cursor

    @FXML
    public void handleMouseEntered() {
        gridPane.setCursor(Cursor.HAND);
    }

    // Quando o cursor do mouse sair de cima de algum botão, iremos voltar o cursor para o normal

    @FXML
    public void handleMouseExited() {
        gridPane.setCursor(Cursor.DEFAULT);
    }

        // Inicializacao de Campos de acordo com o contato selecionado pelo usuario na Janela principal

    void inicializarCampos(){

        nomeField.setText(contato.getNome());
        if (!contato.getSexo().equalsIgnoreCase("Masculino")){
            femininoRadio.setSelected(true);
        }

        numeroField.setText(contato.getNumero());
        emailField.setText(contato.getEmail());
        if (contato.getDescricao() != null){
            descricaoField.setText(contato.getDescricao());
        }

        salvarButton.setDisable(true);
    }

    // Método auxiliar para exibir os alerts. Foi criado para evitar muita repetição de código

    private void exibirAlert(Alert.AlertType alertType, String title, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.initOwner(gridPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();

    }

    void setContato(Contato contato) {
        this.contato = contato;
    }
}
