/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raphaelcollin.contatos;

import java.awt.Dimension;
import java.io.IOException;

import com.raphaelcollin.contatos.model.ContatoDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/janela_principal.fxml"));

            /* Colocando a janela com 30% da largura do dispositivo em que esta sendo executada a aplicacao
             * Colocando a janela com 60% da altura do dispositivo em que esta sendo executada a aplicacao */

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.width * 0.3;
        double heigth = screenSize.height * 0.6;

        Scene scene = new Scene(root,width,heigth);
        
        primaryStage.setTitle("Contatos");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image ("file:arquivos/icon.png"));
        primaryStage.show();
    }

    // Finalizando a conexão com o banco de dados

    @Override
    public void stop(){
        ContatoDAO.getInstance().closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
