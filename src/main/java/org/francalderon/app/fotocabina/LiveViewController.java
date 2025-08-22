package org.francalderon.app.fotocabina;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.services.ServiceManager;
import org.francalderon.app.fotocabina.services.WebcamService;
import org.francalderon.app.fotocabina.utils.FullScreen;

import java.io.IOException;


public class LiveViewController {
    @FXML
    WebcamService webcamService;

    @FXML
    StackPane contenedorLive;

    @FXML
    ImageView imageLive;

    @FXML
    Label temporizador;

    public void initialize() throws IOException {
        ServiceManager serviceManager = ServiceManager.getInstance();
        webcamService = serviceManager.getWebcamService();
        webcamService.setCountDownLabel(temporizador);
        webcamService.setImageView(imageLive);
        imageLive.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
        imageLive.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());

        Platform.runLater(() -> {
            Scene scene = imageLive.getScene();
            Stage stage = (Stage) imageLive.getScene().getWindow();
            FullScreen.activar(scene, stage, imageLive);

        });
    }
}
