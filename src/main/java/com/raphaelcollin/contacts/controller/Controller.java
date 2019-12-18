/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.Optional;

public abstract class Controller {

    public static final int FROM_LEFT = 1;
    public static final int FROM_RIGHT = 2;

    private Timeline timeline = new Timeline();

    Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    protected double getRootWidth() {
        return screenSize.getWidth() * 0.25;
    }

    protected double getRootHeight() {
        return screenSize.getWidth() * 0.25 * 1.11;
    }

    protected Optional<ButtonType> showAlert(Alert.AlertType type, String title, String headerText, String contentText, Parent root){
        Alert alert = new Alert(type);
        alert.initOwner(root.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    protected void changeView(AnchorPane containerRoot, Parent outRoot, Parent inRoot, int option, EventHandler<ActionEvent> actionEvent) {

        double translateXInRoot;
        double translateXOutRoot;

        if (option == FROM_LEFT) {
            translateXInRoot = -containerRoot.getScene().getWidth();
            translateXOutRoot = +containerRoot.getScene().getWidth();
        } else { // FROM_RIGHT and Anything else
            translateXInRoot = containerRoot.getScene().getWidth();
            translateXOutRoot = -containerRoot.getScene().getWidth();
        }

        inRoot.setTranslateX(translateXInRoot);
        outRoot.setTranslateX(0.0);

        containerRoot.getChildren().add(inRoot);

        KeyValue keyValue1 = new KeyValue(inRoot.translateXProperty(), 0.0, Interpolator.EASE_IN);
        KeyValue keyValue2 = new KeyValue(outRoot.translateXProperty(), translateXOutRoot);
        KeyFrame frame1 = new KeyFrame(Duration.millis(800), keyValue1, keyValue2);

        timeline.getKeyFrames().setAll(frame1);

        timeline.setOnFinished(actionEvent);

        timeline.play();

    }
}
