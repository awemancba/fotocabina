package org.francalderon.app.fotocabina.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.service.ArchivoService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuardadorArchivo {
    public static void configPlantilla(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo");
        fileChooser.setInitialFileName("config");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plantilla", "*.txt"));

        File ruta = fileChooser.showSaveDialog(stage);
        if (ruta != null){
            Path origen = Paths.get(Plantilla.CONFIGURACION_TXT);
            Path destino = ruta.toPath();
            ArchivoService.copiarArchivo(origen, destino);
        }else {
            System.out.println("No se ha cargado el archivo");
        }


    }
}
