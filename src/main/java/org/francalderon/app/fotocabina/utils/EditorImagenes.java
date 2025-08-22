package org.francalderon.app.fotocabina.utils;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.Plantilla;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EditorImagenes {
    private Plantilla plantilla;

    public EditorImagenes() {
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void cambiarTamanio(int width) {
        Node nodo = plantilla.getGaleria().get(plantilla.getImgSelected()).getChildren().getFirst();
        ImageView imageView = (ImageView) nodo;
        double largoActual = imageView.getFitHeight();
        System.out.println(largoActual);
        imageView.setFitHeight(largoActual + width);
    }

    public static BufferedImage recortarApectRatio(BufferedImage imagen, Double aspectRatioWidth, Double aspectRatioHeight) {
        if (imagen == null) {
            throw new IllegalArgumentException("La imagen no puede ser null");
        }

        if (aspectRatioWidth == null || aspectRatioHeight == null || aspectRatioWidth <= 0 || aspectRatioHeight <= 0) {
            throw new IllegalArgumentException("Aspect ratio inválido: ancho y largo deben ser mayores a cero");
        }

        int height = imagen.getHeight();
        int width = imagen.getWidth();

        if (height > width) {
            int nuevoLargo = (int) Math.round(width * aspectRatioWidth / aspectRatioHeight);
            nuevoLargo = Math.min(nuevoLargo, imagen.getHeight());

            int y = (imagen.getHeight() - nuevoLargo) / 2;

            return imagen.getSubimage(0, y, width, nuevoLargo);

        } else {
            int nuevoAncho = (int) Math.round(height * aspectRatioWidth / aspectRatioHeight);

            // Asegurarse de que el nuevo ancho no exceda el original
            nuevoAncho = Math.min(nuevoAncho, imagen.getWidth());

            int x = (imagen.getWidth() - nuevoAncho) / 2;

            return imagen.getSubimage(x, 0, nuevoAncho, height);
        }
    }

    public static BufferedImage aplicarEspejoHorizontal(BufferedImage original) {
        BufferedImage espejo = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = espejo.createGraphics();
        g.drawImage(original,
                original.getWidth(), 0,
                0, original.getHeight(),
                0, 0,
                original.getWidth(), original.getHeight(),
                null);
        g.dispose();
        return espejo;
    }

    public static BufferedImage recortar13to10(BufferedImage imagen){
        return EditorImagenes.recortarApectRatio(imagen,13.0,10.0);
    }

    public void cambiarPosicion(double dx, double dy) {
        StackPane imagen = plantilla.getGaleria().get(plantilla.getImgSelected());
        ImageView fondo = plantilla.getFondo();

        double nuevoX = imagen.getLayoutX() + dx;
        double nuevoY = imagen.getLayoutY() + dy;

        double limitX = fondo.getBoundsInParent().getWidth();
        double limitY = fondo.getBoundsInParent().getHeight();

        if (nuevoX >= 0 && nuevoX <= limitX && nuevoY >= 0 && nuevoY <= limitY) {
            imagen.setLayoutX(nuevoX);
            imagen.setLayoutY(nuevoY);
        }
    }
}
