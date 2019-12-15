/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerContainer extends Controller implements Initializable {
    @FXML
    private AnchorPane root;

    private static final String LOCATION_LOADING_VIEW = "/loading_contacts.fxml";

    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        root.setPrefSize(getRootWidth(), getRootHeight());
        root.setMinSize(getRootWidth(), getRootHeight());
        root.setMaxSize(getRootWidth(), getRootHeight());

        this.bundle = resourceBundle;

    }

    public void getContactsList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOCATION_LOADING_VIEW), bundle);
            Parent loadingView = loader.load();
            root.getChildren().add(loadingView);

            AnchorPane.setLeftAnchor(loadingView, 0.0);
            AnchorPane.setRightAnchor(loadingView, 0.0);
            AnchorPane.setTopAnchor(loadingView, 0.0);
            AnchorPane.setBottomAnchor(loadingView, 0.0);

            ControllerLoading controller = loader.getController();
            controller.getContactsList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
