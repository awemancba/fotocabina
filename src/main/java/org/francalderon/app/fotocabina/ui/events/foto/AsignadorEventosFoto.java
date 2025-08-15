package org.francalderon.app.fotocabina.ui.events.foto;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.service.PlantillaService;

import java.util.List;

public class AsignadorEventosFoto {
    public static void selected(StackPane imagen, int index, Plantilla plantilla){
        imagen.setOnMouseClicked(e -> {
            plantilla.setImgSelected(index);
            List<StackPane> galeria = plantilla.getGaleria();
            for (int j = 0; j < galeria.size(); j++) {
                StackPane stackPane = galeria.get(j);
                for (Node nodo : stackPane.getChildren()) {
                    if (nodo instanceof Label) {
                        nodo.setVisible(j == index);
                    }
                }
            }
        });
    }

    public static void arrastrarFoto(PlantillaService plantillaService,StackPane imagen){
        final Delta delta = new Delta();
        imagen.setOnMousePressed(e2 -> {
            delta.x = e2.getSceneX() - imagen.getLayoutX();
            delta.y = e2.getSceneY() - imagen.getLayoutY();
            plantillaService.actualizarConfig();
        });

        imagen.setOnMouseDragged(e3 -> {
            imagen.setLayoutX(e3.getSceneX() - delta.x);
            imagen.setLayoutY(e3.getSceneY() - delta.y);
            plantillaService.actualizarConfig();
        });
    }


    private static class Delta {
        double x, y;
    }
}
