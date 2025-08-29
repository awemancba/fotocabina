package org.francalderon.app.fotocabina.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;


public class FullScreen {
    public static void activar(Scene scene, Stage stage, ImageView imageView) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F10) {
                Screen monitorActual = detectarMonitor(stage);
                imageView.setFitHeight(monitorActual.getBounds().getHeight());
                imageView.setFitWidth(monitorActual.getBounds().getWidth());
                stage.setFullScreen(true);
            }
        });
    }

    public static Screen detectarMonitor(Stage stage) {
        return Screen.getScreens().stream()
                .max(Comparator.comparing(screen -> getArea(stage, screen.getBounds())))
                .orElse(Screen.getPrimary());
    }

    private static double getArea(Stage stage, Rectangle2D monitorBounds) {
        Rectangle2D ventanaBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());

        double interseccionX = Math.max(monitorBounds.getMinX(), ventanaBounds.getMinX());
        double interseccionY = Math.max(monitorBounds.getMinY(), ventanaBounds.getMinY());

        double largoX = Math.max(0, Math.min(monitorBounds.getMaxX(), ventanaBounds.getMaxX()) - interseccionX);
        double largoY = Math.max(0, Math.min(monitorBounds.getMaxY(), ventanaBounds.getMaxY()) - interseccionY);

        return largoX * largoY;
    }
}
