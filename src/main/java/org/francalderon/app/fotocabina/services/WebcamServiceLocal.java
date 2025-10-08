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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService cameraExecutor;

    // --- Constantes para mayor claridad ---
    private static final int TARGET_FPS = 30;
    private static final int FRAME_INTERVAL_MS = 1000 / TARGET_FPS;

    public WebcamServiceLocal(Plantilla plantilla) {
        this.plantilla = plantilla;
        this.temporizador = new Temporizador();
        this.modoEspejo = false;
        this.imageView = new ImageView();
        this.miniPreview = new ImageView();

        iniciarCamara();

    }

    private void iniciarCamara() {
        List<Webcam> webcams = Webcam.getWebcams();
        System.out.println("Cámaras detectadas: " + webcams);

        this.webcam = webcams.getFirst();

        if (this.webcam == null) {
            throw new RuntimeException("No se encontró ninguna cámara compatible.");
        }

        System.out.println("Webcam seleccionada: " + webcam.getName());
        this.webcam.setViewSize(new Dimension(640, 480));
    }

    @Override
    public void start() {
        if (webcam == null || webcam.isOpen()) {
            System.out.println("La cámara no está disponible o ya está en uso.");
            return;
        }

        this.cameraExecutor = Executors.newSingleThreadExecutor();
        webcam.open();
        cameraExecutor.submit(this::procesarFlujoDeVideo);
    }

    private void procesarFlujoDeVideo() {
        while (webcam.isOpen()) {
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
                Thread.sleep(FRAME_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restablece el estado de interrupción
                break; // Sale del bucle si el hilo es interrumpido
            }
        }
    }

    @Override
    public void stop() {
        if (cameraExecutor != null && !cameraExecutor.isShutdown()) {
            cameraExecutor.shutdownNow();
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    @Override
    public void iniciarSecuenciaDeCaptura() {
        new Thread(() -> {
            int cantidadFotos = plantilla.getGaleria().size();
            List<Image> fotosCapturadas = new ArrayList<>();

            for (int i = 0; i < cantidadFotos; i++) {
                ejecutarCuentaRegresiva();
                fotosCapturadas.add(imageView.getImage());
            }

            procesarYGuardarGaleria(fotosCapturadas);
        }).start(); // No olvides iniciar el hilo
    }

    private void ejecutarCuentaRegresiva() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> temporizador.iniciar(latch::countDown));
        try {
            latch.await(); // Espera a que termine la animación en el hilo de JavaFX
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void procesarYGuardarGaleria(List<Image> fotos) {
        this.plantilla.setGaleria(fotos);
        Platform.runLater(() -> {
            activarDesactivarNodo.textOff(plantilla.getGaleria());
            ExportarPlantilla.guardarComoPNG(plantilla);
            activarDesactivarNodo.textOn(plantilla.getGaleria());
            temporizador.getCountDown().setOpacity(1.0);
            temporizador.getCountDown().setText("Bienvenido");
        });
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

    /*@Override
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
    }*/

    private Image procesarImagen(BufferedImage original) {
        BufferedImage recortado = EditorImagenes.recortarApectRatio(original, aspectRatio);
        if (modoEspejo) {
            recortado = EditorImagenes.aplicarEspejoHorizontal(recortado);
        }
        return SwingFXUtils.toFXImage(recortado, null);
    }

}
