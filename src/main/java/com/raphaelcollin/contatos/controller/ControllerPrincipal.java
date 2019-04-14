package com.raphaelcollin.contatos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private Button addButton;
    @FXML
    private JFXListView<GridPane> listView;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXButton excluirButton;
    @FXML
    private JFXButton abrirButton;

        // Tamanho atual da tela

    private Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        // Lista de GridPanes que aparecerão na ListView

    private ObservableList<GridPane> gridList;
        
    public void initialize(){

            // Colocando o tamanho do Text Field principal de acordo com o tamanho da tela

        double width;
        double heigth;

        width = dimension.width * 0.20;
        heigth = dimension.height * 0.05;

            // Criando efeito de sombra que será usado no textField e na listView

        DropShadow dropShadow = new DropShadow(1.8, Color.GRAY);

        textField.setPrefWidth(width);
        textField.setPrefHeight(heigth);

        textField.setEffect(dropShadow);

            // Colocando a imagem de dentro do Botao de acordo com o tamnho da Tela

        ImageView imageView = new ImageView(new Image("file:arquivos/plus-icon.png"));

        imageView.setFitWidth(heigth * 0.80);
        imageView.setFitHeight(heigth * 0.80);

        addButton.setGraphic(imageView);

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

            // Sombra no listView

        listView.setEffect(dropShadow);

            /* Configurando para que quando um item da listView, nesse caso um GridPane, estiver ativo
               os labels do nome e do número fiquem com a cor branca
               quando não estiverem selecionados, o nome ficará preto e o número ficar cinza */

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

            /* Criando Thread para carregar os contatos do banco de dados. Enquanto a Task estiver sendo executada,
            *  um progress indicator ficará aparecendo na tela. Quando a Task terminar sua execução, iremos esconder
            *  o progress indicator e colocar a lista de contatos no lugar*/

        Task<ObservableList<Contato>> task = new Task<ObservableList<Contato>>() {
            @Override
            protected ObservableList<Contato> call(){
                borderPane.setCursor(Cursor.WAIT);
                return ContatoDAO.getInstance().selectContatos();
            }
        };

        task.setOnSucceeded(e -> {

            borderPane.setCursor(Cursor.DEFAULT);
            gridList = FXCollections.observableArrayList();
            ObservableList<Contato> contatos = task.getValue();

                /* Se não houver nenhum contato adicionado a lista, exibiremos um label informando isso.
                   Caso contrário, vamos carregar os contatos */

            if (contatos.size() > 0){
                for (Contato contato : contatos){

                        // Criando e configurando um item da lista (GridPane)

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/contato_item.fxml"));
                    try {
                        GridPane gridPane = fxmlLoader.load();
                        ControllerContato controllerContato = fxmlLoader.getController();

                        controllerContato.getIdField().setText(String.format("%d",contato.getId()));
                        controllerContato.getNomeField().setText(contato.getNome());
                        controllerContato.getSexoField().setText(contato.getSexo());
                        controllerContato.getNumeroField().setText(contato.getNumero());
                        controllerContato.getEmailField().setText(contato.getEmail());
                        controllerContato.getDescricaoField().setText(contato.getDescricao());

                        controllerContato.setImage();

                        gridList.add(gridPane);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                    /* Criando filtro para lista de contatos. Caso o textField esteja vazio ou o conteúdo do textField
                       não corresponder a nenhum contato da lista, iremos exibir todos os contatos. Caso contrário,
                       exibiremos apenas os contatos cujo o nome contém a string contida no textField */

                FilteredList<GridPane> filteredList = new FilteredList<>(gridList,p -> true);

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(textField.getText().trim().isEmpty()){
                        filteredList.setPredicate(gridPane -> true);
                    } else {
                        if (newValue != null){
                            filteredList.setPredicate(gridPane -> {
                                String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();
                                return nome.toLowerCase().contains(newValue.toLowerCase());
                            });
                        }

                    }
                    if (listView.getItems().size() == 0){
                        filteredList.setPredicate(gridPane -> true);
                    }
                    listView.getSelectionModel().selectFirst();
                });

                listView.setItems(filteredList);

                    // Selecionado Primeiro Elemento

                listView.getSelectionModel().selectFirst();

                    // Deixando a lista visível

                listView.setVisible(true);


            } else {

                Label label = new Label("Nenhum contato foi adicionado");
                label.setFont(new Font(20));

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(label);

                ((GridPane) borderPane.getCenter()).getChildren().add(hBox);
                GridPane.setConstraints(hBox,0,1,2,1);
            }

            // Escondendo Progress Indicator

            spinner.setVisible(false);

        });

            // Iniciando Thread


        new Thread(task).start();

            // Configurando as dimensões e o tamanho da fonte dos botões abrir e excluir contato

        excluirButton.setPrefWidth(dimension.width * 0.3 / 2 - 54);
        excluirButton.setFont(new Font(dimension.width * 0.012));
        excluirButton.setPrefHeight(40);
        abrirButton.setPrefWidth(dimension.width * 0.3 / 2 - 54);
        abrirButton.setFont(new Font(dimension.width * 0.012));
        abrirButton.setPrefHeight(40);

        
    }

        // Quando o botao Adicionar for acionado vamos carregar a outra janela com o mesmo tamanho da atual para o
        // usuario adiconar seu contato

    @FXML
    public void handleButton() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        try {
            Parent adiconarJanela = FXMLLoader.load(getClass().getResource("/janela_adicionar.fxml"));
            stage.setScene(new Scene(adiconarJanela, dimension.width * 0.3, dimension.height * 0.6));
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
        * Se o usuario confirmar a operacao, vamos tentar excluir o contato do banco, se a operacao for realizado
        * com sucesso, vamos exibir uma mensagem de sucesso e se ocorrer algum erro, vamos exibir uma mensagem de erro
        * */

    @FXML
    public void handleExcluir(){

        // Obtendo elemento selecionado

        GridPane gridPane = listView.getSelectionModel().getSelectedItem();

        if (gridPane != null){

            // Obtendo Id e nome do contato selecionado

            int intId = Integer.parseInt(((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(0)).getText());
            String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();

            // Exibindo Confirmação

            Optional<ButtonType> result = exibirAlert(Alert.AlertType.CONFIRMATION,"Confirmação",
                    "Tem certeza que deseja excluir o contato " + nome + "?",
                    "Clique em Ok para confirmar e em Cancelar para voltar");


            if (result.isPresent() && result.get().equals(ButtonType.OK)){
                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call() {
                        return ContatoDAO.getInstance().deleteContato(intId);
                    }
                };

                task.setOnSucceeded(event -> {

                    if (task.getValue()){
                        gridList.remove(gridPane);
                    } else {
                        exibirAlert(Alert.AlertType.ERROR,"Erro no processamento",nome,
                                "Ocorreu um erro ao tentar excluir esse contato. Tente mais tarde");
                    }
                });

                // Iniciando Thread

                new Thread(task).start();

            }
        }

    }

        /* Quando esse metodo for chamado, vamos carregar outra janela onde sera possivel o usuario ver as informacoes
         * do usuario selecionado */

    private void abrirJanelaEditarContato(){

        // Obtendo elemento selecionado

        GridPane gridPane = listView.getSelectionModel().getSelectedItem();

        if (gridPane != null){

            // Obtendo Atributos

            int intId = Integer.parseInt(((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(0)).getText());

            String nome = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(1)).getText();
            String sexo = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(2)).getText();
            String numero = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(3)).getText();
            String email = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(4)).getText();
            String descricao = ((Label) ((VBox) gridPane.getChildren().get(1)).getChildren().get(5)).getText();

            Contato contato = new Contato(intId,nome,sexo,numero,email,descricao);

            Stage stage = (Stage) borderPane.getScene().getWindow();
            FXMLLoader fxmlLoader;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/janela_detalhes.fxml"));
                GridPane root = fxmlLoader.load();

                ControllerDetalhes controllerDetalhes = fxmlLoader.getController();
                controllerDetalhes.setContato(contato);

                // Inicializando campos com os respectivos atributos

                controllerDetalhes.inicializarCampos();

                Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

                double width = screenSize.width * 0.3;
                double heigth = screenSize.height * 0.6;

                stage.setScene(new Scene(root,width,heigth));

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

    // Quando o cursor mouse estiver sobre algum botão, iremos colocar trocar o cursor

    @FXML
    public void handleMouseEntered() {
        borderPane.setCursor(Cursor.HAND);
    }

    // Quando o cursor do mouse sair de cima de algum botão, iremos voltar o cursor para o normal

    @FXML
    public void handleMouseExited() {
        borderPane.setCursor(Cursor.DEFAULT);
    }

    // Método auxiliar para exibir os alerts. Foi criado para evitar muita repetição de código

    private Optional<ButtonType> exibirAlert(Alert.AlertType type, String title, String headerText, String contentText){
        Alert alert = new Alert(type);
        alert.initOwner(borderPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }
}
