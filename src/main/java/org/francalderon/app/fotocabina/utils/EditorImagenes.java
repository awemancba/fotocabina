package org.francalderon.app.fotocabina.utils;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;

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

    public void cambiarTamanio(double width, double height) {
        Node nodo = plantilla.getGaleria().get(plantilla.getImgSelected()).getChildren().getFirst();
        ImageView imageView = (ImageView) nodo;
        double altoActual = imageView.getFitHeight();
        double anchoActual = imageView.getFitWidth();
        imageView.setFitHeight(altoActual + height);
        imageView.setFitWidth(anchoActual + width);
    }

    public static BufferedImage recortarApectRatio(BufferedImage imagen, AspectRatio aspectRatio) {
        if (imagen == null) {
            throw new IllegalArgumentException("La imagen no puede ser null");
        }

        if (aspectRatio.getAspectWidth() == null || aspectRatio.getAspectHeight() == null || aspectRatio.getAspectWidth() <= 0 || aspectRatio.getAspectHeight() <= 0) {
            throw new IllegalArgumentException("Aspect ratio inválido: ancho y largo deben ser mayores a cero");
        }

        int originalHeight = imagen.getHeight();
        int originalWidth = imagen.getWidth();

        double targetRatio = aspectRatio.getAspectRatio();

        int recorteWidth, recorteHeight;
        int x, y;

        recorteHeight = originalHeight;
        recorteWidth = (int) Math.round(recorteHeight * targetRatio);

        if (recorteWidth <= originalWidth) {
            x = (originalWidth - recorteWidth) / 2;
            y = 0;

        } else {
            recorteWidth = originalWidth;
            recorteHeight = (int) Math.round(recorteWidth / targetRatio);
            x = 0;
            y = (originalHeight - recorteHeight) / 2;
        }
        return imagen.getSubimage(x, y, recorteWidth, recorteHeight);
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
