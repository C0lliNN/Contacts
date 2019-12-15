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
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerDashboard extends Controller implements Initializable {


    @FXML
    private BorderPane root;
    @FXML
    private TextField textField;
    @FXML
    private ImageView imageView;
    @FXML
    private ListView<GridPane> listView;

        // Tamanho atual da tela

    private Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

        // Lista de GridPanes que aparecerão na ListView

    private ObservableList<GridPane> gridList;

        // Constantes

    private static final String LOCATION_ICON_ADD = "/add-contact-icon.png";
    private static final String URL_ARQUIVO_CONTATO_FXML = "/contact_item.fxml";
    private static final String LOCATION_ADD_CONTACT_VIEW = "/add_contact.fxml";
    private static final String URL_JANELA_DETALHES = "/details_contact.fxml";

    private static final String ID_IMAGEM_ADICIONAR_CONTATO = "imagem-adicionar-contato";
    private static final String CLASS_BOTAO_EXCLUIR_CONTATO =  "botao-laranja";
    private static final String CLASS_BOTAO_ABRIR_CONTATO =  "botao-azul";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        root.setPrefSize(getRootWidth(), getRootHeight());
        root.setMinSize(getRootWidth(), getRootHeight());
        root.setMaxSize(getRootWidth(), getRootHeight());

            // Padding e Margin

        root.setPadding(new Insets(tamanhoTela.getHeight() * 0.0185,0.0,0.0,
                0.0));
        BorderPane.setMargin(root.getCenter(), new Insets(tamanhoTela.getHeight() * 0.0185,0.0,
                tamanhoTela.getHeight() * 0.0185, 0.0));

            // Alinhando e definindo tamanho de controles

        AnchorPane.setLeftAnchor(textField,tamanhoTela.getWidth() * 0.025);
        AnchorPane.setRightAnchor(textField, tamanhoTela.getWidth() * 0.07);
        AnchorPane.setLeftAnchor(imageView, tamanhoTela.getWidth() * 0.1);
        AnchorPane.setTopAnchor(imageView, tamanhoTela.getHeight() * 0.00648);

        textField.setPrefWidth(tamanhoTela.getWidth() * 0.20);
        textField.setPrefHeight(tamanhoTela.getHeight() * 0.05);
        textField.setFont(new Font("Arial",tamanhoTela.getWidth() * 0.01145));

        imageView.setImage(new Image(getClass().getResourceAsStream(LOCATION_ICON_ADD)));

        imageView.setFitWidth(tamanhoTela.getHeight() * 0.068);
        imageView.setFitHeight(tamanhoTela.getHeight() * 0.065);

            // Criando efeito de sombra

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        textField.setEffect(dropShadow);
        imageView.setEffect(dropShadow);


            // Colocando ContextMenu para os items do List View

        listView.setOnContextMenuRequested( event -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Abrir Contato");
            editItem.setOnAction(event2 -> abrirJanelaEditarContato());
            MenuItem deleteItem = new MenuItem("Excluir Contato");
            deleteItem.setOnAction(event3 -> handleExcluirContato());
            contextMenu.getItems().addAll(editItem, deleteItem);
            contextMenu.show(root.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        });

            // Sombra no listView

        listView.setEffect(dropShadow);

            /* Configurando para que quando um item da listView estiver ativo
               os labels do nome e do número fiquem com a cor branca quando não estiverem selecionados,
               o nome ficará preto e o número ficar cinza */

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null){
                for (Node label : ((VBox) newValue.getChildren().get(1)).getChildren()){
                    label.setStyle("-fx-text-fill: white");
                }
            }

            if (oldValue != null){
                ((VBox) oldValue.getChildren().get(1)).getChildren().get(1).setStyle("-fx-text-fill: #333333");
                ((VBox) oldValue.getChildren().get(1)).getChildren().get(3).setStyle("-fx-text-fill: #7a7a7a");
            }

        });

        // Difinindo Classes CSS

        imageView.setId(ID_IMAGEM_ADICIONAR_CONTATO);

        
    }

        // Quando o botao Adicionar for acionado a janela para adição de umo novo contato será carregada

    @FXML
    public void handleAdicionarContato() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_ADD_CONTACT_VIEW));
            Parent addView = loader.load();
            ControllerAddContact controllerAddContact = loader.getController();

            loader = new FXMLLoader(getClass().getResource("/contact_fields.fxml"));
            loader.load();

            ControllerContactFields controllerContactFields = loader.getController();
            controllerAddContact.setFields(controllerContactFields);

            AnchorPane containerRoot = (AnchorPane) root.getScene().getRoot();
            changeView(containerRoot, root, addView, FROM_RIGHT);

        } catch (IOException e){
            System.out.println("Erro: " + e.getMessage());
        }

    }

        /* Se o usuário der dois cliques em cima de um item da lista, será executado o método abrirJanelaEditarContato()
           que irá carregar uma janela na qual será possível que o usuário veja informações do contato selecionado */

    @FXML
    public void handleListDoubleClick(MouseEvent event){
        if (event.getClickCount() == 2) {
            abrirJanelaEditarContato();
        }
    }

        /* Se o usuário acionar o botao abrir contato, será executado o método abrirJanelaEditarContato()
           que irá carregar uma janela na qual será possível que o usuário veja informações do contato selecionado */

    @FXML
    public void handleAbrirContato(){
        abrirJanelaEditarContato();
    }

        /*
        * Quando o usuário acionar o botão Excluir Contato, será exibido um alert de confirmação,
        * Se o usuário confirmar a operação, será feita uma tentativa de exclusão do contato no banco de dados, se a tentativa obtiver
        * sucesso, será exibida uma mensagem de sucesso e se ocorrer algum erro, vamos exibir uma mensagem de erro
        * */

    @FXML
    public void handleExcluirContato(){

        // Obtendo elemento selecionado

        GridPane gridPane = listView.getSelectionModel().getSelectedItem();

        if (gridPane != null){

            // Obtendo Id e nome do contato selecionado

            int intId = Integer.parseInt(((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(0)).getText());
            String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();

            // Exibindo Confirmação

            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,"Confirmação",
                    "Tem certeza que deseja excluir o contato " + nome + "?",
                    "Clique em Ok para confirmar e em Cancelar para voltar", root);


            if (result.isPresent() && result.get().equals(ButtonType.OK)){
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() {
                        DAO<Contact> dao = new ContactDAO();
                        return dao.delete(intId);
                    }
                };

                task.setOnSucceeded(event -> {

                    if (task.getValue()){
                        gridList.remove(gridPane);
                    } else {
                        showAlert(Alert.AlertType.ERROR,"Erro no processamento",nome,
                                "Ocorreu um erro ao tentar excluir esse contato. Tente mais tarde", root);
                    }
                });

                // Iniciando Thread

                new Thread(task).start();

            }
        }

    }

        /* Quando esse metodo for chamado, será carregada outra janela onde sera possível visualizar informações
         * do usuário selecionado */

    private void abrirJanelaEditarContato(){

        // Obtendo elemento selecionado

        GridPane gridPane = listView.getSelectionModel().getSelectedItem();

        if (gridPane != null){

            int intId = Integer.parseInt(((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(0)).getText());

            String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();
            String sexo = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(2)).getText();
            String numero = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(3)).getText();
            String email = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(4)).getText();
            String descricao = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(5)).getText();

            Contact contact = new Contact(intId,nome,sexo,numero,email,descricao);

            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource(URL_JANELA_DETALHES));
                GridPane root = fxmlLoader.load();

                ControllerContactDetails controllerContactDetails = fxmlLoader.getController();
                controllerContactDetails.setContact(contact);

                // Inicializando campos com os respectivos atributos

                controllerContactDetails.inicializarCampos();

                stage.getScene().setRoot(root);

            } catch (IOException e){
                System.out.println("Erro: " + e.getMessage());
            }
        }

    }

    // Se o usuário apertar a tecla Delete do teclado, será executado o método para excluir o contato
    // Se o usuário apertar a tecla Enter do teclado, será executado o método para abrir o contato

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DELETE)){
            handleExcluirContato();
        }
        if(event.getCode().equals(KeyCode.ENTER)){
            handleAbrirContato();
        }
    }

    public void setupContacts(ObservableList<GridPane> gridList) {
        this.gridList = gridList;
        listView.setItems(gridList);
    }
}
