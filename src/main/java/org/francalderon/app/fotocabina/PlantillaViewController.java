package org.francalderon.app.fotocabina;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.service.ArchivoService;
import org.francalderon.app.fotocabina.service.PlantillaService;
import org.francalderon.app.fotocabina.service.ServiceManager;
import org.francalderon.app.fotocabina.utils.AdminVentanas;

import java.io.IOException;
import java.nio.file.Paths;

public class PlantillaViewController {
    @FXML
    PlantillaService plantillaService;
    Plantilla plantilla;

    public void initialize() throws IOException {
        plantillaService = ServiceManager.getInstance().getPlantillaService();
        plantilla = ServiceManager.getInstance().getPlantilla();

    }

    @FXML
    Button btnCambiarFondo;

    @FXML
    Button btn9x13;

    @FXML
    Button btn10x15;

    @FXML
    Button btn13x18;

    @FXML
    ImageView image1;

    @FXML
    ImageView image2;

    @FXML
    ImageView image3;

    @FXML
    protected void onCambiarFondoClick() {
        String urlImage = ArchivoService.copyToResources(AdminVentanas.getPlantillaView());
        Image nuevoFondo = new Image(urlImage);
        Platform.runLater(() -> plantilla.getFondo().setImage(nuevoFondo));
    }

    @FXML
    protected void on9x13Click() {
        plantillaService.cambiarTam9x13();
    }

    @FXML
    protected void on10x15Click() {
        plantillaService.cambiarTam10x15();
    }

    @FXML
    protected void on13x18Click() {
        plantillaService.cambiarTam13x18();
    }

}
