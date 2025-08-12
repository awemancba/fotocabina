package org.francalderon.app.fotocabina.utils;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class FullScreen {
    public static void activar(Scene scene, Stage stage, ImageView imageView){
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F10) {
                imageView.setFitHeight(Screen.getPrimary().getBounds().getHeight());
                imageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
                stage.setFullScreen(true);
            }
        });
    }
}
