package org.francalderon.app.fotocabina.ui.events.foto;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.services.PlantillaService;

public class EliminarComponente {
    public static void foto(Stage stage, Scene scene, PlantillaService plantillaService) {
        scene.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.DELETE){
                plantillaService.removerFoto();
            }
        });
    }
}
