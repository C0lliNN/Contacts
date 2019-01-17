
package com.raphaelcollin.contatos.controller;

import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class ControllerPrincipal {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField textField;
    @FXML
    private Button searchButton;
    @FXML

    private ListView<Contato> listView;

        // Tamanho atual da tela

    private Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    private ObservableList<Contato> contatosList;
        
    public void initialize(){

            // Colocando o tamanho do Text Field principal de acordo com o tamanho da tela

        double width;
        double heigth;

        width = screenSize.width * 0.20;
        heigth = screenSize.height * 0.05;

        textField.setPrefWidth(width);
        textField.setPrefHeight(heigth);

            // Colocando a imagem de dentro do Botao de acordo com o tamnho da Tela

        ImageView imageView = new ImageView(new Image(getClass().
                getResourceAsStream("/com/raphaelcollin/contatos/imagens/add.png")));

        imageView.setFitWidth(heigth * 0.80);
        imageView.setFitHeight(heigth * 0.80);

        searchButton.setGraphic(imageView);

            // Pesquisando Contatos de Acordo com o conteudo do Text Field

        textField.textProperty().addListener( (observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()){
                ObservableList<Contato> contatos = FXCollections.observableArrayList();
                for (Contato contato : contatosList){
                    if (contato.getNome().toLowerCase().indexOf(textField.getText().toLowerCase()) == 0){
                        contatos.add(contato);
                    }
                }
                listView.itemsProperty().unbind();
                listView.itemsProperty().set(contatos);
            } else {
                listView.itemsProperty().set(contatosList);
            }
            listView.getSelectionModel().selectFirst();
        });


            // Colocando Menus de Contexto para List View

        listView.setOnContextMenuRequested( event -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Abrir Contato");
            editItem.setOnAction(event2 -> abrirJanelaEditarContato());
            MenuItem deleteItem = new MenuItem("Excluir Contato");
            deleteItem.setOnAction(event3 -> handleExcluir());
            contextMenu.getItems().addAll(editItem, deleteItem);
            contextMenu.show(borderPane.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        });

            // Carregando os Contatos do banco de Dados

        Task<ObservableList<Contato>> task = new Task<ObservableList<Contato>>() {
            @Override
            protected ObservableList<Contato> call(){
                return ContatoDAO.getInstance().selectContatos();
            }
        };

        task.setOnSucceeded(e -> listView.getSelectionModel().selectFirst());

            // Lista auxilar para fazer a pesquisa em tempo real dos contatos

        contatosList = ContatoDAO.getInstance().selectContatos();

            // Utilizando outro Thread para fazer a leitura dos dados no Banco de Dados

        listView.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

        
    }

        // Quando o botao Adicionar for acionado vamos carregar a outra janela com o mesmo tamanho da atual para o
        // usuario adiconar seu contato

    @FXML
    public void handleButton() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        try {
            Parent adiconarJanela = FXMLLoader.load(getClass().getResource("/com/raphaelcollin/contatos/view/janela_adicionar.fxml"));
            stage.setScene(new Scene(adiconarJanela,screenSize.width * 0.3, screenSize.height * 0.6));
        } catch (IOException e){
            System.out.println("Erro: " + e.getMessage());
        }

    }

        // Se o usuario der dois cliques em cima de um item da lista, vamos executar o metodo abrirJanelaEditarContato()
        // que ira abrir uma nova janela que sera possivel que o usuario veja informacoes do contato selecionado

    @FXML
    public void handleListDoubleClick(MouseEvent event){
        if (event.getClickCount() == 2) {
            abrirJanelaEditarContato();
        }
    }

        // Se o usuario acionar o botao abrir contato, vamos executar o metodo abrirJanelaEditarContato()
        // que ira abrir uma nova janela que sera possivel que o usuario veja informacoes do contato selecionado

    @FXML
    public void handleAbrirContato(){
        abrirJanelaEditarContato();
    }

        /*
        * Quando o usuario acionar o botao Excluir Contato, sera exibido um alert de confirmacao,
        * Se o usuario confirmar a operacao, vamos tentar excluir o contato do banco, se a operacao for realizado com sucesso,
        * vamos exibir uma mensagem de sucesso e se ocorrer algum erro, vamos exibir uma mensagem de erro
        * */

    @FXML
    public void handleExcluir(){

        Contato contato = listView.getSelectionModel().getSelectedItem();

        if (contato != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(borderPane.getScene().getWindow());
            alert.setTitle("Confirmação");
            alert.setHeaderText("Tem certeza que deseja excluir o contato " + contato + "?");
            alert.setContentText("Clique em Ok para confirmar e em Cancelar para voltar");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)){
                boolean result2 = ContatoDAO.getInstance().deleteContato(contato.getId());
                if (result2){
                    listView.getItems().remove(contato);
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.initOwner(borderPane.getScene().getWindow());
                    alert1.setTitle("Erro no processamento");
                    alert1.setTitle("Não foi possível excluir o contato " + listView.getSelectionModel().getSelectedItem());
                    alert1.setTitle("Ocorreu um erro ao tentar excluir esse contato. Tente mais tarde");
                    alert.show();
                }
            }
        }

    }

        /* Quando esse metodo for chamado, vamos carregar outra janela onde sera possivel o usuario ver as informacoes
         * do usuario selecionado */

    private void abrirJanelaEditarContato(){
        Contato contato = listView.getSelectionModel().getSelectedItem();

        if (contato != null){
            Stage stage = (Stage) borderPane.getScene().getWindow();
            FXMLLoader fxmlLoader;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/raphaelcollin/contatos/view/janela_detalhes.fxml"));
                GridPane gridPane = fxmlLoader.load();

                ControllerDetalhes controllerDetalhes = fxmlLoader.getController();
                controllerDetalhes.inicializarCampos(contato);

                Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

                double width = screenSize.width * 0.3;
                double heigth = screenSize.height * 0.6;

                stage.setScene(new Scene(gridPane,width,heigth));

            } catch (IOException e){
                System.out.println("Erro: " + e.getMessage());
            }
        }

    }

    // Se o usuario apertar a tecla Delete do teclado, vamos executar o metodo para excluir o contato
    // Se o usuario apertar a tecla Enter do teclado, vamos executar o metodo para abrir o contato

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DELETE)){
            handleExcluir();
        }
        if(event.getCode().equals(KeyCode.ENTER)){
            handleAbrirContato();
        }
    }

}
