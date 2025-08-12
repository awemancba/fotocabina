package org.francalderon.app.fotocabina;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.francalderon.app.fotocabina.service.PlantillaService;
import org.francalderon.app.fotocabina.service.ServiceManager;

public class PlantillaViewController {
    @FXML
    PlantillaService plantillaService;

    public void initialize(){
        plantillaService = ServiceManager.getInstance().getPlantillaService();
    }

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
    protected void on9x13Click(){
        plantillaService.cambiarTam9x13();
    }

    @FXML
    protected void on10x15Click(){
        plantillaService.cambiarTam10x15();
    }

    @FXML
    protected void on13x18Click(){
        plantillaService.cambiarTam13x18();
    }

}
