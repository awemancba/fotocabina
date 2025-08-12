package org.francalderon.app.fotocabina;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Controles");
        primaryStage.show();

        FXMLLoader liveView = new FXMLLoader(getClass().getResource("/fxml/LiveView.fxml"));
        Parent live = liveView.load();
        Stage ventanaVivo = new Stage();
        ventanaVivo.setScene(new Scene(live));
        ventanaVivo.setTitle("Vivo");
        ventanaVivo.show();
    }
}
