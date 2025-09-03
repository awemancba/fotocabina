package org.francalderon.app.fotocabina.ui.events.foto;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.services.PlantillaService;

import java.util.List;

public class AsignadorEventosFoto {
    public static void selected(StackPane imagen, int index, Plantilla plantilla) {
        imagen.setOnMouseClicked(e -> {
            plantilla.setImgSelected(index);
            List<StackPane> galeria = plantilla.getGaleria();
            for (int j = 0; j < galeria.size(); j++) {
                StackPane stackPane = galeria.get(j);
                for (Node nodo : stackPane.getChildren()) {
                    if (nodo instanceof Label) {
                        if (j == index) {
                            ((Label) nodo).setText("Select " + (index + 1) );
                        } else {
                            ((Label) nodo).setText(String.valueOf(galeria.indexOf(stackPane) + 1 ));
                        }
                    }
                }
            }
        });
    }

    public static void arrastrarFoto(PlantillaService plantillaService, StackPane imagen) {
        final Delta delta = new Delta();
        imagen.setOnMousePressed(e2 -> {
            Point2D local = imagen.getParent().sceneToLocal(e2.getSceneX(),e2.getSceneY());
            delta.x = local.getX() - imagen.getLayoutX();
            delta.y = local.getY() - imagen.getLayoutY();
            plantillaService.actualizarConfig();
        });

        imagen.setOnMouseDragged(e3 -> {
            Point2D local = imagen.getParent().sceneToLocal(e3.getSceneX(),e3.getSceneY());
            imagen.setLayoutX(local.getX() - delta.x);
            imagen.setLayoutY(local.getY() - delta.y);
            plantillaService.actualizarConfig();
        });
    }

    private static class Delta {
        double x, y;
    }
}
