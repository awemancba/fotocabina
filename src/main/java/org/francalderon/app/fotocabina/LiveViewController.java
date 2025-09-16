package org.francalderon.app.fotocabina;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.services.ServiceManager;
import org.francalderon.app.fotocabina.services.WebCamServiceSocket;
import org.francalderon.app.fotocabina.services.WebcamServiceLocal;
import org.francalderon.app.fotocabina.services.interfaces.WebcamService;
import org.francalderon.app.fotocabina.utils.FullScreen;

import java.io.IOException;


public class LiveViewController {
    @FXML
    WebcamService webcamService;
    WebCamServiceSocket webCamServiceSocket;

    @FXML
    StackPane contenedorLive;

    @FXML
    ImageView imageLive;

    @FXML
    Label temporizador;

    public void initialize() throws IOException {
        ServiceManager serviceManager = ServiceManager.getInstance();
        webcamService = serviceManager.getWebcamService();
        webCamServiceSocket = serviceManager.getWebCamServiceSocket();
        webCamServiceSocket.setImageView(imageLive);

        webcamService.getTemporizador().setCountDown(temporizador);
        //webcamService.setImageView(imageLive);
        imageLive.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
        imageLive.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());

        Platform.runLater(() -> {
            Scene scene = imageLive.getScene();
            Stage stage = (Stage) imageLive.getScene().getWindow();
            FullScreen.activar(scene, stage, imageLive);

            DoubleProperty fontSize = new SimpleDoubleProperty();
            fontSize.bind(imageLive.getScene().widthProperty().multiply(0.182));
            temporizador.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
            webcamService.getTemporizador().getIcono().fitHeightProperty().bind(imageLive.getScene().widthProperty().multiply(0.26));
        });
    }
}
