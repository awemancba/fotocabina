package org.francalderon.app.fotocabina;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import org.francalderon.app.fotocabina.utils.AdminVentanas;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load();
        AdminVentanas.setMainViewController(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Controles");
        primaryStage.show();
    }
}
