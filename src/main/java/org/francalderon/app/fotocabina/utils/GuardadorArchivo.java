package org.francalderon.app.fotocabina.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.services.ArchivoJsonService;
import org.francalderon.app.fotocabina.services.ArchivoTxtService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuardadorArchivo {
    public static void plantillaTxt(Stage stage) {
        guardarArchivo(stage, "*.txt", Plantilla.CONFIGURACION_TXT);
    }

    public static void plantillaJson(Stage stage) {
        guardarArchivo(stage, "*.json", Plantilla.CONFIGURACION_JSON);
    }

    private static void guardarArchivo(Stage stage, String extensions, String config) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo");
        fileChooser.setInitialFileName("config");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plantilla", extensions));

        File ruta = fileChooser.showSaveDialog(stage);
        if (ruta != null) {
            Path origen = Paths.get(config);
            Path destino = ruta.toPath();
            ArchivoJsonService.copiarArchivo(origen, destino);
        } else {
            System.out.println("No se ha cargado el archivo");
        }
    }
}
