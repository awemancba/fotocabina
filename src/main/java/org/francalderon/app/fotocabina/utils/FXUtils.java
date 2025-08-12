package org.francalderon.app.fotocabina.utils;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class FXUtils {
    public static Stage obternerStage(Node node){
        return (Stage) node.getScene().getWindow();
    }
    public static double calcularRelacion(Image nuevo, Image actual){
        return nuevo.getHeight() / actual.getHeight();
    }
}
