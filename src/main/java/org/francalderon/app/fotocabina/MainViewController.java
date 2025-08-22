package org.francalderon.app.fotocabina;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.PlantillaDTO;
import org.francalderon.app.fotocabina.services.PlantillaService;
import org.francalderon.app.fotocabina.services.ServiceManager;
import org.francalderon.app.fotocabina.services.WebcamService;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;
import org.francalderon.app.fotocabina.ui.events.foto.EliminarComponente;
import org.francalderon.app.fotocabina.utils.*;

import java.io.File;
import java.io.IOException;

public class MainViewController {
    @FXML
    Plantilla plantilla;
    EditorImagenes editorImagenes;
    ArchivoService<PlantillaDTO> archivoService;
    PlantillaService plantillaService;
    WebcamService webcamService;

    public void initialize() throws IOException {
        ServiceManager serviceManager = ServiceManager.getInstance();
        plantilla = serviceManager.getPlantilla();
        editorImagenes = serviceManager.getEditorImagenes();
        archivoService = serviceManager.getArchivoService();
        plantillaService = serviceManager.getPlantillaService();
        webcamService = serviceManager.getWebcamService();
        serviceManager.iniciarServicios();

        File archivo = new File(Plantilla.CONFIGURACION_JSON);
        plantillaService.cargarConfig(archivo);

        imageMiniPreview.imageProperty().bind(webcamService.getMiniPreview().imageProperty());
        plantillaLive.prefWidthProperty().bind(plantilla.prefWidthProperty());
        plantillaLive.prefHeightProperty().bind(plantilla.prefHeightProperty());
        plantillaLive.getChildren().add(plantilla);

        Platform.runLater(() -> {
            Scene scene = btnPlantilla.getScene();
            Stage stage = (Stage) scene.getWindow();
            EliminarComponente.foto(stage, scene, plantillaService);

            obtenerStage().setOnCloseRequest(e->{
                webcamService.stop();
                if (AdminVentanas.getVentanaVivo() != null){
                    AdminVentanas.getVentanaVivo().close();
                }
            });

        });

    }

    @FXML
    private CheckBox openLive;

    @FXML
    private Label labelMiniPreview;

    @FXML
    private Button btnVerFotos;

    @FXML
    private Button btnCaptura;

    @FXML
    private Button btnPlantilla;

    @FXML Button btnFotos;

    @FXML
    private Button btnGuardarPlantilla;

    @FXML
    private Button btnCargarPlantilla;

    @FXML
    private Button btnImprimir;

    @FXML
    private Button btnAjustes;

    @FXML
    private HBox contenedorPrincipal;

    @FXML
    private Pane plantillaLive;

    @FXML
    private VBox controles;

    @FXML
    private Pane contenedorCaptura;

    @FXML
    private ImageView iconoCaptura;

    @FXML
    private ImageView imageMiniPreview;

    @FXML
    private StackPane contenedorMiniPreview;

    @FXML
    protected void onOpenLiveClick() throws IOException {
        AdminVentanas.toggleLiveWindow(openLive);
    }

    @FXML
    protected void onCapturaClick() {
        webcamService.tomarFotosConTemporizador();
    }

    @FXML
    protected void onPlantillaClick() throws IOException {
        AdminVentanas.plantillaView(obtenerStage());
    }

    @FXML
    protected void onGuardarPlantillaClick() {
        archivoService.guardarConfig();
    }

    @FXML
    protected void onCargarPlantillaClick(){
        plantillaService.cargarPlantilla();
    }

    @FXML
    protected void onImprimirClick() {
        plantillaService.agregarFoto();
    }

    @FXML
    protected void onVerFotosClick() {
        AbrirCarpeta.verFotos();
    }

    @FXML
    protected void onFotosClick() throws IOException {
        AdminVentanas.imageControlView(obtenerStage());
    }

    @FXML
    protected void onAjustesClick() {
        Stage stage = (Stage) btnAjustes.getScene().getWindow();
        Image nuevoIcono = SelectorArchivo.seleccionarImagen(stage);
        if (nuevoIcono != null){
            webcamService.getIcono().setImage(nuevoIcono);
        } else {
            System.out.println("No se puede cargar la imagen");
        }
    }

    private Stage obtenerStage() {
        return (Stage) btnCaptura.getScene().getWindow();
    }
}
