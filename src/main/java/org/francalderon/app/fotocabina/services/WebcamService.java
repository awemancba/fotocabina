package org.francalderon.app.fotocabina.services;

import com.github.sarxos.webcam.Webcam;
import javafx.animation.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.francalderon.app.fotocabina.models.Plantilla;
import javafx.scene.control.Label;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;
import org.francalderon.app.fotocabina.ui.events.foto.activarDesactivarNodo;
import org.francalderon.app.fotocabina.utils.EditorImagenes;
import org.francalderon.app.fotocabina.utils.ExportarPlantilla;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class WebcamService {
    private Webcam webcam;
    private ImageView imageView;
    private ImageView miniPreview;
    private Thread videoThread;
    private boolean running = false;
    private Plantilla plantilla;
    private ImageView icono;
    private Label countDown;
    private int tiempo;
    private AspectRatio aspectRatio = AspectRatio.FOTO4_3;
    private boolean modoEspejo;

    public WebcamService(int width, int height, Plantilla plantilla) {
        this.plantilla = plantilla;

        tiempo = 3;
        modoEspejo = false;

        icono = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/img/icono.png")).toExternalForm()));
        icono.setFitHeight(500);
        icono.setPreserveRatio(true);

        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(width, height));

        imageView = new ImageView();
        miniPreview = new ImageView();

        for (Dimension d : webcam.getViewSizes()) {
            System.out.println("Resolución disponible: " + d.width + "x" + d.height);
        }
    }

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

    public void stop() {
        running = false;
        if (videoThread != null) {
            videoThread.interrupt();
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getMiniPreview() {
        return miniPreview;
    }

    public void setCountDownLabel(Label countDownLabel) {
        this.countDown = countDownLabel;
    }

    public ImageView getIcono() {
        return icono;
    }

    public AspectRatio getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public boolean isRunning() {
        return running;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setModoEspejo(boolean modoEspejo) {
        this.modoEspejo = modoEspejo;
    }

    public void tomarFotosConTemporizador() {
        int cantidadFotos = plantilla.getGaleria().size();
        List<Image> fotos = new ArrayList<>();
        new Thread(() -> {
            for (int i = 0; i < cantidadFotos; i++) {
                CountDownLatch latch = new CountDownLatch(1);
                Platform.runLater(() -> temporizador(latch::countDown));
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
                countDown.setOpacity(1.0);
                countDown.setText("Bienvenido");
            });
        }).start();
    }


    public void temporizador(Runnable cuandoTermina) {
        // Mostrar mensaje inicial
        countDown.setText("Preparense");
        countDown.setOpacity(1.0);

        // Transición de preparación
        FadeTransition preparacion = new FadeTransition(Duration.seconds(tiempo),countDown);
        preparacion.setFromValue(1.0);
        preparacion.setToValue(0.0);
        preparacion.setOnFinished(e -> {
            countDown.setText("");
            iniciarCuentaRegresiva(cuandoTermina);
        });

        preparacion.play();
    }

    private void iniciarCuentaRegresiva(Runnable cuandoTermina) {
        SequentialTransition secuencia = new SequentialTransition();

        // Conteo regresivo: 3, 2, 1
        for (int i = 3; i > 0; i--) {
            int numero = i;

            PauseTransition mostrarNumero = new PauseTransition(Duration.seconds(0.1));
            mostrarNumero.setOnFinished(e -> {
                countDown.setText(String.valueOf(numero));
                countDown.setOpacity(1.0);
            });

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.9), countDown);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            secuencia.getChildren().addAll(mostrarNumero, fadeOut);
        }

        // Mostrar ícono final
        PauseTransition mostrarIcono = new PauseTransition(Duration.seconds(0.1));
        mostrarIcono.setOnFinished(e -> {
            countDown.setText("");
            countDown.setGraphic(icono);
            countDown.setOpacity(1.0);
        });

        FadeTransition ocultarIcono = new FadeTransition(Duration.seconds(1), countDown);
        ocultarIcono.setFromValue(1.0);
        ocultarIcono.setToValue(0.0);
        ocultarIcono.setOnFinished(e -> {
            countDown.setGraphic(null);
            cuandoTermina.run();
        });

        secuencia.getChildren().addAll(mostrarIcono, ocultarIcono);
        secuencia.play();
    }

    private Image procesarImagen(BufferedImage original) {
        BufferedImage recortado = EditorImagenes.recortarApectRatio(original, aspectRatio);
        if (modoEspejo) {
            recortado = EditorImagenes.aplicarEspejoHorizontal(recortado);
        }
        return SwingFXUtils.toFXImage(recortado, null);
    }


}
