package org.francalderon.app.fotocabina.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;

public class SelectorImagen {
    public static Image seleccionarImagen(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cambiar fondo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            return new Image(archivo.toURI().toString());
        }
        return null;
    }

    public static String seleccionarImagenURL(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File archivo = fileChooser.showOpenDialog(stage);
        if (archivo != null) {
            return archivo.toURI().toString();
        }
        return null;
    }


}
