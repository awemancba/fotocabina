package org.francalderon.app.fotocabina.services;

import com.github.sarxos.webcam.Webcam;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.francalderon.app.fotocabina.models.Plantilla;
import javafx.scene.control.Label;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;
import org.francalderon.app.fotocabina.ui.events.foto.DesactivarNodo;
import org.francalderon.app.fotocabina.utils.EditorImagenes;
import org.francalderon.app.fotocabina.utils.ExportarPlantilla;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private AspectRatio aspectRatio = AspectRatio.FOTO4_3;

    public WebcamService(int width, int height, Plantilla plantilla) {
        this.plantilla = plantilla;
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

                    BufferedImage recortado = EditorImagenes.recortarApectRatio(original, aspectRatio);
                    BufferedImage mirrored = EditorImagenes.aplicarEspejoHorizontal(recortado);
                    Image fxImage = SwingFXUtils.toFXImage(mirrored, null);
                    Platform.runLater(() -> {
                        imageView.setImage(fxImage);

                        if (miniPreview.isVisible()){
                            miniPreview.setImage(fxImage);
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

    public ImageView getImageView() {
        return imageView;
    }

    public ImageView getMiniPreview() {
        return miniPreview;
    }

    public Label getCountDownLabel() {
        return countDown;
    }

    public void setCountDownLabel(Label countDownLabel) {
        this.countDown = countDownLabel;
    }

    public void setIcono(ImageView imageView) {
        this.icono = imageView;
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

    public void temporizador(double tiempo, Runnable cuandoTermina) {
        int[] temporizador = {3};
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> countDown.setText("Preparense")
                        , new KeyValue(countDown.opacityProperty(), 1.0)),

                new KeyFrame(Duration.seconds(tiempo), temp0 -> countDown.setText(""), new KeyValue(countDown.opacityProperty(), 0.0)),

                new KeyFrame(Duration.seconds(tiempo + 1), e -> {
                    Timeline timeLineInterno = new Timeline(
                            new KeyFrame(Duration.seconds(0), temp -> {
                                if (temporizador[0] > 0) {
                                    countDown.setText(String.valueOf(temporizador[0]));
                                    temporizador[0]--;
                                } else {
                                    countDown.setText("");
                                    countDown.setGraphic(icono);
                                }
                            }),
                            new KeyFrame(Duration.seconds(1), new KeyValue(countDown.opacityProperty(), 1.0)),
                            new KeyFrame(Duration.seconds(2), new KeyValue(countDown.opacityProperty(), 0.0))
                    );
                    timeLineInterno.setCycleCount(4);
                    timeLineInterno.setOnFinished(action2 -> countDown.setGraphic(null));
                    timeLineInterno.play();
                }),
                new KeyFrame(Duration.seconds(tiempo + 9), new KeyValue(countDown.opacityProperty(), 1.0))
        );
        timeline.setOnFinished(action -> cuandoTermina.run());
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void tomarFotosConTemporizador() {
        int cantidadFotos = plantilla.getGaleria().size();
        List<BufferedImage> fotos = new ArrayList<>();
        new Thread(() -> {
            for (int i = 0; i < cantidadFotos; i++) {
                CountDownLatch latch = new CountDownLatch(1);
                Platform.runLater(() -> temporizador(5, latch::countDown));
                try {
                    latch.await(); // Espera a que termine la animación
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }

                BufferedImage imagen = webcam.getImage();
                BufferedImage recortado = EditorImagenes.recortarApectRatio(imagen, aspectRatio);
                BufferedImage mirrored = EditorImagenes.aplicarEspejoHorizontal(recortado);

                fotos.add(mirrored);
            }
            this.plantilla.setGaleria(fotos);
            Platform.runLater(() -> {
                DesactivarNodo.texto(plantilla.getGaleria());
                ExportarPlantilla.guardarComoPNG(plantilla);
                countDown.setOpacity(1.0);
                countDown.setText("Bienvenido");
            });

        }).start();

    }


}
