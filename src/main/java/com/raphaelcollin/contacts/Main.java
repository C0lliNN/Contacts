/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raphaelcollin.contatos;

import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;



public class Main extends Application {

    // Constantes

    private static final String URL_JANELA_PRINCIPAL = "/janela_principal.fxml";
    private static final String URL_ESTILO_CSS = "/estilo.css";
    private static final String TITULO_STAGE = "Contatos";
    private static final String URL_ICONE = "file:arquivos/icon.png";

    @Override
    public void start(Stage primaryStage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(URL_JANELA_PRINCIPAL));

            /* Colocando a janela com 30% da largura do dispositivo em que esta sendo executada a aplicacao
             * Colocando a janela com 60% da altura do dispositivo em que esta sendo executada a aplicacao */

        Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();
        double largura = tamanhoTela.getWidth() * 0.3;
        double altura = tamanhoTela.getWidth() * 0.3 * 1.125;

        Scene scene = new Scene(root,largura,altura);
        scene.getStylesheets().add(getClass().getResource(URL_ESTILO_CSS).toExternalForm());
        
        primaryStage.setTitle(TITULO_STAGE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image (URL_ICONE));
        primaryStage.show();
    }

    // Finalizando a conexão com o banco de dados

    @Override
    public void stop(){
        try {
            ContatoDAO.getInstance().closeConnection();
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
