/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts;

import com.raphaelcollin.contacts.controller.ControllerContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FXMain extends Application {
    // Constants

    private static final String LOCATION_CONTAINER = "/base_container.fxml";
    private static final String LOCATION_STYLE_CSS = "/style.css";
    private static final String LOCATION_STAGE_ICON = "/icon.png";

    @Override
    public void start(Stage stage) throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle("lang", new Locale("en", "US"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_CONTAINER), bundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ControllerContainer controller = loader.getController();
        controller.getContactsList();

        stage.setTitle(bundle.getString("stage_title"));
        scene.getStylesheets().add(getClass().getResource(LOCATION_STYLE_CSS).toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(LOCATION_STAGE_ICON)));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
