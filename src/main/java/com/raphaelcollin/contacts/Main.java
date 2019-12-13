/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts;

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

    // Constants

    private static final String LOCATION_DASHBOARD = "/dashboard.fxml";
    private static final String URL_ESTILO_CSS = "/estilo.css";
    private static final String TITULO_STAGE = "Contatos";
    private static final String URL_ICONE = "/icon.png";

    @Override
    public void start(Stage stage) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource(LOCATION_DASHBOARD));

        Rectangle2D tamanhoTela = Screen.getPrimary().getBounds();
        double width = tamanhoTela.getWidth() * 0.3; // 30%
        double height = tamanhoTela.getWidth() * 0.3 * 1.125; // Keep aspect radio

        Scene scene = new Scene(root,width,height);
        scene.getStylesheets().add(getClass().getResource(URL_ESTILO_CSS).toExternalForm());
        
        stage.setTitle(TITULO_STAGE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image (getClass().getResourceAsStream(URL_ICONE)));
        stage.show();
    }// Finalizando a conexão com o banco de dados


    public static void main(String[] args) {
        launch(args);
    }
    
}
