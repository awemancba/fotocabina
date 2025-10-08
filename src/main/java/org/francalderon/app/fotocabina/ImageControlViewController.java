package org.francalderon.app.fotocabina;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;
import org.francalderon.app.fotocabina.services.ArchivoJsonService;
import org.francalderon.app.fotocabina.services.PlantillaService;
import org.francalderon.app.fotocabina.services.ServiceManager;
import org.francalderon.app.fotocabina.services.WebcamServiceLocal;
import org.francalderon.app.fotocabina.services.interfaces.WebcamService;

import java.io.IOException;


public class ImageControlViewController {
    @FXML
    PlantillaService plantillaService;
    WebcamService webcamService;
    ArchivoJsonService archivoJsonService;

    public void initialize() throws IOException {
        plantillaService = ServiceManager.getInstance().getPlantillaService();
        webcamService = ServiceManager.getInstance().getWebcamService();
        archivoJsonService = (ArchivoJsonService) ServiceManager.getInstance().getArchivoService();
    }

    @FXML
    private Button btnArriba;

    @FXML
    private Button btnAbajo;

    @FXML
    private Button btnIzquierda;

    @FXML
    private Button btnDerecha;

    @FXML
    private Button btnAchicar;

    @FXML
    private Button btnAgrandar;

    @FXML
    private Label labelMover;

    @FXML
    private Label labelTam;

    @FXML
    private CheckBox selectAll;

    @FXML
    private Button aspect1_1;

    @FXML
    private Button aspect4_3;

    @FXML
    private Button aspect16_9;


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

    @FXML
    protected void On1_1Click() throws IOException {
        webcamService.setAspectRatio(AspectRatio.FOTO1_1);
        plantillaService.actualizarConfig();
        plantillaService.cambiarFotos1_1();
        archivoJsonService.enviarConfig(MainViewController.ESPERA);
    }

    @FXML
    protected void On4_3Click() throws IOException {
        webcamService.setAspectRatio(AspectRatio.FOTO4_3);
        plantillaService.actualizarConfig();
        plantillaService.cambiarFotos4_3();
        archivoJsonService.enviarConfig(MainViewController.ESPERA);
    }

    @FXML
    protected void On16_9Click() throws IOException {
        webcamService.setAspectRatio(AspectRatio.FOTO16_9);
        plantillaService.actualizarConfig();
        plantillaService.cambiarFotos16_9();
        archivoJsonService.enviarConfig(MainViewController.ESPERA);
    }


}
