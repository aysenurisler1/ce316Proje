package com.kaanege.iae;

import com.kaanege.iae.db.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseManager.initializeDatabase();

        InputStream iconStream = getClass().getResourceAsStream("/icon.png");
        if (iconStream != null) {
            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);
        } else {
            System.out.println("icon.png not found in resources.");
        }

        MainView mainView = new MainView();

        Scene scene = new Scene(mainView.getRoot(), 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("IAE - Integrated Assignment Environment");
        stage.setScene(scene);
        stage.setMinWidth(950);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}