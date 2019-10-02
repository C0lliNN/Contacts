package com.raphaelcollin.contatos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.raphaelcollin.contatos.model.Contato;
import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ControllerPrincipal {


    @FXML
    private BorderPane root;
    @FXML
    private TextField textField;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXListView<GridPane> listView;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXButton excluirButton;
    @FXML
    private JFXButton abrirButton;

        // Tamanho atual da tela

    private Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();

        // Lista de GridPanes que aparecerão na ListView

    private ObservableList<GridPane> gridList;

        // Constantes

    private static final String URL_IMAGEM_ICONE_MAIS = "file:arquivos/add-contact-icon.png";
    private static final String URL_ARQUIVO_CONTATO_FXML = "/contato_item.fxml";
    private static final String URL_JANELA_ADICIONAR = "/janela_adicionar.fxml";
    private static final String URL_JANELA_DETALHES = "/janela_detalhes.fxml";

    private static final String ID_IMAGEM_ADICIONAR_CONTATO = "imagem-adicionar-contato";
    private static final String CLASS_BOTAO_EXCLUIR_CONTATO =  "botao-laranja";
    private static final String CLASS_BOTAO_ABRIR_CONTATO =  "botao-azul";
        
    public void initialize(){

            // Padding e Margin

        root.setPadding(new Insets(tamanhoTela.getHeight() * 0.0185,0.0,tamanhoTela.getHeight() * 0.0185,
                0.0));
        BorderPane.setMargin(root.getCenter(), new Insets(tamanhoTela.getHeight() * 0.0185,0.0,
                tamanhoTela.getHeight() * 0.0185, 0.0));

            // Alinhando e definindo tamanho de controles

        AnchorPane.setLeftAnchor(textField,tamanhoTela.getWidth() * 0.025);
        AnchorPane.setRightAnchor(textField, tamanhoTela.getWidth() * 0.07);
        AnchorPane.setLeftAnchor(imageView, tamanhoTela.getWidth() * 0.25);
        AnchorPane.setTopAnchor(imageView,- tamanhoTela.getHeight() * 0.00648);

        textField.setPrefWidth(tamanhoTela.getWidth() * 0.20);
        textField.setPrefHeight(tamanhoTela.getHeight() * 0.05);
        textField.setFont(new Font("Arial",tamanhoTela.getWidth() * 0.01145));

        excluirButton.setPrefWidth(tamanhoTela.getWidth() * 0.121875);
        excluirButton.setFont(new Font(tamanhoTela.getWidth() * 0.012));
        excluirButton.setPrefHeight(tamanhoTela.getHeight() * 0.03703);

        abrirButton.setPrefWidth(tamanhoTela.getWidth() * 0.121875);
        abrirButton.setFont(new Font(tamanhoTela.getWidth() * 0.012));
        abrirButton.setPrefHeight(tamanhoTela.getHeight() * 0.03703);

        imageView.setImage(new Image(URL_IMAGEM_ICONE_MAIS));

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
        excluirButton.getStyleClass().add(CLASS_BOTAO_EXCLUIR_CONTATO);
        abrirButton.getStyleClass().add(CLASS_BOTAO_ABRIR_CONTATO);

            /* Criando Thread para carregar os contatos do banco de dados. Enquanto a Task estiver sendo executada,
            *  um progress indicator ficará aparecendo na tela. Quando a Task terminar sua execução, caso tenha sido executada com sucesso,
            *  o progressIndicator será escondido e será colocado a lista de contatos no lugar. Caso contrário, um alert será exibido
            *  indicando erro na conexão com o Banco de Dados e aplicação será fechada */

        Task<ObservableList<Contato>> task = new Task<ObservableList<Contato>>() {
            @Override
            protected ObservableList<Contato> call(){
                root.setCursor(Cursor.WAIT);
                return ContatoDAO.getInstance().recuperarTodosContatos();
            }
        };

        task.setOnSucceeded(e -> {

            root.setCursor(Cursor.DEFAULT);
            gridList = FXCollections.observableArrayList();
            ObservableList<Contato> contatos = task.getValue();

            if (contatos == null) {

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_CENTER);
                Label label = new Label("Erro na Conexão com o Banco de Dados");
                label.setFont(new Font(tamanhoTela.getWidth() * 0.01354));
                hBox.getChildren().setAll(label);

                GridPane gridPane = (GridPane) root.getCenter();
                gridPane.setVgap(tamanhoTela.getHeight() * 0.04629);
                gridPane.getChildren().remove(spinner);
                gridPane.getChildren().add(hBox);
                GridPane.setConstraints(hBox,0,1,2,1);

                exibirAlert(Alert.AlertType.ERROR,"Contatos","Erro no Banco de Dados",
                        "Não foi possível se conectar ao Banco de Dados MYSQL");

                Platform.exit();
                return;
            }

                /* Se não houver nenhum contato adicionado a lista, um label será exibindo informando esse fato.
                   Caso contrário, os contatos serão carregados */

            if (contatos.size() > 0){
                for (Contato contato : contatos){

                        // Criando e configurando um item da lista (GridPane)

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(URL_ARQUIVO_CONTATO_FXML));
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
                        System.err.println("Erro: " + ex.getMessage());
                    }
                }

                    /* Criando filtro para lista de contatos. Caso o textField esteja vazio ou o conteúdo do textField
                       não corresponder a nenhum contato da lista, serão exibir todos os contatos. Caso contrário,
                       apenas os contatos cujo o nome contém a string contida no textField será exibido */

                FilteredList<GridPane> filteredList = new FilteredList<>(gridList,p -> true);

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(textField.getText().trim().isEmpty()){
                        filteredList.setPredicate(gridPane -> true);
                    } else {
                        if (newValue != null){
                            filteredList.setPredicate(gridPane -> {
                                String nome = ((Label) ((VBox) gridPane.getChildren().
                                        get(1)).getChildren().get(1)).getText();
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


            } else { // Caso em que a lista de Contatos está vazia

                Label label = new Label("Nenhum contato foi adicionado");
                label.setFont(new Font(tamanhoTela.getWidth() * 0.01354));

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(label);

                ((GridPane) root.getCenter()).getChildren().add(hBox);
                GridPane.setConstraints(hBox,0,1,2,1);
            }

            // Escondendo Progress Indicator

            spinner.setVisible(false);

        });

            // Iniciando Thread


        new Thread(task).start();

        
    }

        // Quando o botao Adicionar for acionado a janela para adição de umo novo contato será carregada

    @FXML
    public void handleAdicionarContato() {
        Stage stage = (Stage) root.getScene().getWindow();
        try {
            Parent adiconarJanela = FXMLLoader.load(getClass().getResource(URL_JANELA_ADICIONAR));
            stage.getScene().setRoot(adiconarJanela);
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

            Optional<ButtonType> result = exibirAlert(Alert.AlertType.CONFIRMATION,"Confirmação",
                    "Tem certeza que deseja excluir o contato " + nome + "?",
                    "Clique em Ok para confirmar e em Cancelar para voltar");


            if (result.isPresent() && result.get().equals(ButtonType.OK)){
                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call() {
                        return ContatoDAO.getInstance().excluirContato(intId);
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

        /* Quando esse metodo for chamado, será carregada outra janela onde sera possível visualizar informações
         * do usuário selecionado */

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

            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader fxmlLoader;
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource(URL_JANELA_DETALHES));
                GridPane root = fxmlLoader.load();

                ControllerDetalhes controllerDetalhes = fxmlLoader.getController();
                controllerDetalhes.setContato(contato);

                // Inicializando campos com os respectivos atributos

                controllerDetalhes.inicializarCampos();

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

    // Método auxiliar para exibir os alerts. Foi criado para evitar repetição de código

    private Optional<ButtonType> exibirAlert(Alert.AlertType type, String title, String headerText, String contentText){
        Alert alert = new Alert(type);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }
}
