package org.francalderon.app.fotocabina;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.francalderon.app.fotocabina.service.PlantillaService;
import org.francalderon.app.fotocabina.service.ServiceManager;


public class ImageControlViewController {
    @FXML
    PlantillaService plantillaService;

    public void initialize(){
        plantillaService = ServiceManager.getInstance().getPlantillaService();
    }

    @FXML
    Button btnArriba;

    @FXML
    Button btnAbajo;

    @FXML
    Button btnIzquierda;

    @FXML
    Button btnDerecha;

    @FXML
    Button btnAchicar;

    @FXML
    Button bntAgrandar;

    @FXML
    Label labelMover;

    @FXML
    Label laberTam;

    @FXML
    protected void OnArribaClick(){
        plantillaService.moverArriba();
    }

    @FXML
    protected void OnAbajoClick(){
        plantillaService.moverAbajo();
    }

    @FXML
    protected void OnIzquierdaClick(){
        plantillaService.moverIzquierda();
    }

    @FXML
    protected void OnDerechaClick(){
        plantillaService.moverDerecha();
    }

    @FXML
    protected void OnAgrandarClick(){
        plantillaService.agrandarFoto();
    }

    @FXML
    protected void OnAchicarClick(){
        plantillaService.achicarFoto();
    }
}
