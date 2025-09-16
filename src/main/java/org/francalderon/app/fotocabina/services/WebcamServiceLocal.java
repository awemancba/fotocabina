package org.francalderon.app.fotocabina.services;

import com.github.sarxos.webcam.Webcam;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.Temporizador;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;
import org.francalderon.app.fotocabina.services.interfaces.WebcamService;
import org.francalderon.app.fotocabina.ui.events.foto.activarDesactivarNodo;
import org.francalderon.app.fotocabina.utils.EditorImagenes;
import org.francalderon.app.fotocabina.utils.ExportarPlantilla;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WebcamServiceLocal implements WebcamService {
    private Webcam webcam;
    private ImageView imageView;
    private ImageView miniPreview;
    private Thread videoThread;
    private boolean running = false;
    private Plantilla plantilla;
    private AspectRatio aspectRatio = AspectRatio.FOTO4_3;
    private boolean modoEspejo;
    private Temporizador temporizador;

    public WebcamServiceLocal(int width, int height, Plantilla plantilla) {
        this.plantilla = plantilla;
        this.temporizador = new Temporizador();

        modoEspejo = false;

        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(width, height));

        imageView = new ImageView();
        miniPreview = new ImageView();

        for (Dimension d : webcam.getViewSizes()) {
            System.out.println("Resolución disponible: " + d.width + "x" + d.height);
        }
    }
    @Override
    public void start() {
        if (running) return;
        webcam.open();
        running = true;

        videoThread = new Thread(() -> {
            while (running) {
                BufferedImage original = webcam.getImage();
                if (original != null) {
                    Image imagenProcesada = procesarImagen(original);
                    Platform.runLater(() -> {
                        imageView.setImage(imagenProcesada);
                        if (miniPreview.isVisible()) {
                            miniPreview.setImage(imagenProcesada);
                        }
                    });
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        });
        videoThread.setDaemon(true);
        videoThread.start();
    }

    @Override
    public void stop() {
        running = false;
        if (videoThread != null) {
            videoThread.interrupt();
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    @Override
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getMiniPreview() {
        return miniPreview;
    }

    @Override
    public AspectRatio getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @Override
    public Temporizador getTemporizador() {
        return temporizador;
    }

    public boolean isRunning() {
        return running;
    }
    @Override
    public void setModoEspejo(boolean modoEspejo) {
        this.modoEspejo = modoEspejo;
    }
    @Override
    public void tomarFotosConTemporizador() {
        int cantidadFotos = plantilla.getGaleria().size();
        List<Image> fotos = new ArrayList<>();
        new Thread(() -> {
            for (int i = 0; i < cantidadFotos; i++) {
                CountDownLatch latch = new CountDownLatch(1);
                Platform.runLater(() -> temporizador.iniciar(latch::countDown));
                try {
                    latch.await(); // Espera a que termine la animación
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                fotos.add(imageView.getImage());
            }
            this.plantilla.setGaleria(fotos);

            Platform.runLater(() -> {
                activarDesactivarNodo.textOff(plantilla.getGaleria());
                ExportarPlantilla.guardarComoPNG(plantilla);
                activarDesactivarNodo.textOn(plantilla.getGaleria());
                temporizador.getCountDown().setOpacity(1.0);
                temporizador.getCountDown().setText("Bienvenido");
            });
        }).start();
    }

    private Image procesarImagen(BufferedImage original) {
        BufferedImage recortado = EditorImagenes.recortarApectRatio(original, aspectRatio);
        if (modoEspejo) {
            recortado = EditorImagenes.aplicarEspejoHorizontal(recortado);
        }
        return SwingFXUtils.toFXImage(recortado, null);
    }


}
