package org.francalderon.app.fotocabina.models;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

public class Temporizador {
    private int tiempo;
    private Label countDown;
    private ImageView icono;

    public Temporizador() {
        this.tiempo = 3;
        this.icono = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/img/icono.png")).toExternalForm()));
        icono.setPreserveRatio(true);
        icono.setFitHeight(500);
    }

    public void iniciar(Runnable cuandoTermina) {
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

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setCountDown(Label countDown) {
        this.countDown = countDown;
    }

    public void setIcono(ImageView icono) {
        this.icono = icono;
    }
}
