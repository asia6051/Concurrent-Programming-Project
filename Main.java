package com.example.projektpiwonska;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static AnchorPane animationPane;
    public static Configuration config;
    public static Animation.Status animationStatus=Animation.Status.STOPPED;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        SplitPane root=fxmlLoader.load();
        animationPane=(AnchorPane)root.getItems().get(1);
        Scene scene = new Scene(root, 1200, 900);
        stage.setTitle("Fryzjerzy");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {launch();}


}
