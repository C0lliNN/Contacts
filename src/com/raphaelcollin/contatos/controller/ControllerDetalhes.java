package com.raphaelcollin.contatos.controller;

import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerDetalhes {

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

        // Contato que sera visto seus detalhes

    private Contato contato;

        // Maximo de Caracteres para o campo descricao

    private static final int MAX_TEXTAREA_LENGTH = 245;

    public void initialize(){

            // Imagem

        imageView.setImage(new Image(getClass().
                getResourceAsStream("/com/raphaelcollin/contatos/imagens/details.png")));

            // Colocando Labels para os Campos

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
    }

        /* O metodo sera executado quando o botao Editar for acionado,
        *  Quando isso acontercer, vamos dar a opcao do usuario poder editar os campos do Contato
        *  Trocar o texto do botao para Salvar, trocar seu estilo e seu onAction que sera executado quando o usuario quiser
        *  atualizar os dados do contato
        * */


    @FXML
    public void handleEditar(ActionEvent event){
        nomeField.setEditable(true);
        numeroField.setEditable(true);
        emailField.setEditable(true);
        descricaoField.setEditable(true);

        Button button = (Button) event.getSource();
        button.setText("Salvar");
        button.setId("salvar-button");
        button.setOnAction(event1 -> handleSalvar(event));

        nomeField.requestFocus();
    }

    /*
    * Esse metodo sera executado quando o Botao Salvar for acionado
    * Primeiramente, vamos verfificar se os campos nao estao vazios e se possuem valores validos.
    *
    * Se os campos nao possuirem valores validos, vamos exibir um alert e finalizar o metodo
    * Se os campos forem validos, vamos verificar quais campos foram alterados pelo usuarios para poder montar as queries
    * corretamente.
    *
    * Se nenhum campos for alterado, vamos simplesmente deixar todos os campos no mode de somente leitura e retornar para o botao
    * Editar.
    * Se houver campos alterados, vamos executar as queries uma a uma. Vamos criar duas listas: A lista dos dados atualizados
    * com sucesso e a lista dos dados que nao puderam ser atualizados.
    *
    * Nesse processo podem acontecer erros como: O usuario trocar o nome para um nome que ja esteja cadastrado, etc...
    *
    * No final vamos ter tres tipos de alert diferentes: Se todos os campos forem atualizados com sucesso, Se alguns campos
    * forem atualizados com sucesso e se nenhum campo foi atualizado.
    *
    * Se alguns campos forem atualizados com sucesso, vamos exibir um alert mostrando quais foram alterados e quais nao foram
    * */

    private void handleSalvar(ActionEvent event){
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

        if (erroEncontrado) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(gridPane.getScene().getWindow());
            alert.setTitle("Erro no processamento");
            alert.setHeaderText("Campo inválido");
            alert.setContentText(erro);
            alert.show();
            return;
        }


        if (!(nomeField.getText().equals(contato.getNome()) && numeroField.getText().equals(contato.getNumero())
            && emailField.getText().equals(contato.getEmail()) && ((contato.getDescricao() != null && descricaoField.getText().isEmpty())
                || (contato.getDescricao() != null && descricaoField.getText().equals(contato.getDescricao()))))){

            ContatoDAO contatoDAO = new ContatoDAO();

            String sql;
            boolean result;

                // Lista dos campos que foram atualizados com sucesso e dos que nao foram

            List<String> sucesso = new ArrayList<>();
            List<String> falha = new ArrayList<>();

                // Contador de Queries atualizados com sucesso

            int count = 0;

            if (!nomeField.getText().equals(contato.getNome())){
                sql = "update Contatos set nome = '" + nomeField.getText() + "' where idContato = " + contato.getId();
                result = contatoDAO.updateContato(sql);
                if (result){
                    sucesso.add("Nome");
                    contato.setNome(nomeField.getText());
                } else {
                    falha.add("Nome");
                }
                count++;

            }
            if (!numeroField.getText().equals(contato.getNumero())){
               sql = "update Contatos set numero = '" + numeroField.getText() + "' where idContato = " + contato.getId();
               result = contatoDAO.updateContato(sql);
                if (result){
                    sucesso.add("Número");
                    contato.setNumero(numeroField.getText());
                } else {
                    falha.add("Número");
                }
                count++;
            }
            if(!emailField.getText().equals(contato.getEmail())){
                sql = "update Contatos set email = '" + emailField.getText() + "' where idContato = " + contato.getId();
                result = contatoDAO.updateContato(sql);
                if (result){
                    sucesso.add("E-mail");
                    contato.setEmail(emailField.getText());
                } else {
                    falha.add("E-mail");
                }
                count++;
            }
            if((contato.getDescricao() == null && !descricaoField.getText().trim().isEmpty()) || (contato.getDescricao() != null
                && !descricaoField.getText().equals(contato.getDescricao()))){
                sql = "update Contatos set descricao = '" + descricaoField.getText() + "' where idContato = " + contato.getId();
                result = contatoDAO.updateContato(sql);
                if (result){
                    sucesso.add("Descrição");
                    contato.setDescricao(descricaoField.getText());
                } else {
                    falha.add("Descrição");
                }
                count++;
            }

            contatoDAO.closeConnection();


            if (sucesso.size() == count){
                nomeField.setEditable(false);
                numeroField.setEditable(false);
                emailField.setEditable(false);
                descricaoField.setEditable(false);
                Button button = (Button) event.getSource();
                button.setText("Editar");
                button.setId("adicionar-button");
                button.setOnAction(event1 -> handleEditar(event));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(gridPane.getScene().getWindow());
                alert.setTitle("Processamento Realizado");
                alert.setHeaderText("Contato atualizado com sucesso");
                alert.setContentText("O contato atualizado já está disponível na lista de contatos!");
                alert.show();
            } else if (sucesso.size() >= 1 && sucesso.size() < count) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(gridPane.getScene().getWindow());
                alert.setTitle("Processamento Realizado");
                alert.setHeaderText("Contato atualizado com alguns erros");

                StringBuilder stringBuilder = new StringBuilder(sucesso.get(0));
                for (int c = 1; c < sucesso.size(); c++){
                    stringBuilder.append(", ");
                    stringBuilder.append(sucesso.get(c));
                }

                StringBuilder stringBuilder2 = new StringBuilder(falha.get(0));
                for (int c = 1; c < falha.size(); c++){
                    stringBuilder2.append(", ");
                    stringBuilder2.append(falha.get(c));
                }

                StringBuilder stringBuilderFinal = new StringBuilder("Campos atualizados com sucesso: ");
                stringBuilderFinal.append(stringBuilder.toString());
                stringBuilderFinal.append("\n");
                stringBuilderFinal.append("Campos não atualizados: ");
                stringBuilderFinal.append(stringBuilder2.toString());
                stringBuilderFinal.append("\n");
                stringBuilderFinal.append("Verifique se os novos valores já não estão cadastrados em outros usuários");
                alert.setContentText(stringBuilderFinal.toString());
                alert.show();

                nomeField.setEditable(false);
                numeroField.setEditable(false);
                emailField.setEditable(false);
                descricaoField.setEditable(false);
                Button button = (Button) event.getSource();
                button.setText("Editar");
                button.setId("adicionar-button");
                button.setOnAction(event1 -> handleEditar(event));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(gridPane.getScene().getWindow());
                alert.setTitle("Erro no processamento");
                alert.setHeaderText("Não foi possível atualizar o contato");
                alert.setContentText("Verifique se os novos dados já não estão cadastrados em outros contatos");
                alert.show();
            }
        } else {
            nomeField.setEditable(false);
            numeroField.setEditable(false);
            emailField.setEditable(false);
            descricaoField.setEditable(false);
            Button button = (Button) event.getSource();
            button.setText("Editar");
            button.setId("adicionar-button");
            button.setOnAction(event1 -> handleEditar(event));
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

        // Inicializacao de Campos de acordo com o contato selecionado pelo usuario na Janela principal

    void inicializarCampos(Contato contato){
        this.contato = contato;
        nomeField.setText(contato.getNome());
        numeroField.setText(contato.getNumero());
        emailField.setText(contato.getEmail());
        if (contato.getDescricao() != null){
            descricaoField.setText(contato.getDescricao());
        }
    }
}
