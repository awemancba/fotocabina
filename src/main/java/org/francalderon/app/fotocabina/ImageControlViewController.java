package org.francalderon.app.fotocabina;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.francalderon.app.fotocabina.services.PlantillaService;
import org.francalderon.app.fotocabina.services.ServiceManager;

import java.io.IOException;


public class ImageControlViewController {
    @FXML
    PlantillaService plantillaService;

    public void initialize() throws IOException {
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
    Button btnAgrandar;

    @FXML
    Label labelMover;

    @FXML
    Label labelTam;

    @FXML
    CheckBox selectAll;

    @FXML
    protected void OnArribaClick() {
        if (selectAll.isSelected()) {
            plantillaService.moverTodosArriba();
        } else {
            plantillaService.moverArriba();
        }

    }

    @FXML
    protected void OnAbajoClick() {
        if (selectAll.isSelected()) {
            plantillaService.moverTodosAbajo();
        } else {
            plantillaService.moverAbajo();
        }

    }

    @FXML
    protected void OnIzquierdaClick() {
        if (selectAll.isSelected()) {
            plantillaService.moverTodosIzquierda();
        } else {
            plantillaService.moverIzquierda();
        }
    }

    @FXML
    protected void OnDerechaClick() {
        if (selectAll.isSelected()) {
            plantillaService.moverTodosDerecha();
        } else {
            plantillaService.moverDerecha();
        }
    }

    @FXML
    protected void OnAgrandarClick() {
        plantillaService.agrandarFoto();
    }

    @FXML
    protected void OnAchicarClick() {
        plantillaService.achicarFoto();
    }
}
