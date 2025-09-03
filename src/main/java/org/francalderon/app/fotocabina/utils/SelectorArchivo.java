package org.francalderon.app.fotocabina.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;

public class SelectorArchivo {
    public static Image seleccionarImagen(Stage stage) {
        File archivo = cargarArchivo(stage,"Imagenes","*.png", "*.jpg", "*.jpeg", "*.gif");
        if (archivo != null) {
            return new Image(archivo.toURI().toString());
        }
        System.out.println("No se ha cargado un fondo");
        return null;
    }

    public static File seleccionarImagenFile(Stage stage) {
        return cargarArchivo(stage,"Imagenes","*.png", "*.jpg", "*.jpeg", "*.gif");
    }

    public static File seleccionarConfigFile(Stage stage) {
        return cargarArchivo(stage,"Plantilla","*.txt");
    }

    public static File seleccionarConfigJson(Stage stage){
        return cargarArchivo(stage,"Plantilla","*.json");
    }

    private static File cargarArchivo(Stage stage,String descripcion, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(descripcion, extensions));
        return fileChooser.showOpenDialog(stage);
    }


}
