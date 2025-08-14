package org.francalderon.app.fotocabina.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import org.francalderon.app.fotocabina.models.Plantilla;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportarPlantilla {

    public static void guardarComoPNG(Plantilla plantilla) {

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String resultado = ahora.format(formato);
        String nombre = "plantilla_" + resultado;

        WritableImage imagenEscalada = escalar(plantilla);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imagenEscalada, null);

        try {
            File archivo = new File(System.getProperty("user.home") + "/.fotocabina/" + nombre + ".png");

            ImageIO.write(bufferedImage, "png", archivo);
            System.out.println("Plantilla guardada en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar la imagen: " + e.getMessage());
        }
    }

    public static WritableImage escalar(Plantilla plantilla){
        SnapshotParameters parameters = new SnapshotParameters();
        double escala = plantilla.getTamanioFoto().getAlto() / plantilla.getHeight();

        parameters.setTransform(Transform.scale(escala, escala));

        int ancho = (int) (plantilla.getPrefWidth() * escala);
        int largo = (int) (plantilla.getPrefHeight() * escala);

        WritableImage writableImage = new WritableImage(ancho, largo);
        return plantilla.snapshot(parameters, writableImage);
    }
}
