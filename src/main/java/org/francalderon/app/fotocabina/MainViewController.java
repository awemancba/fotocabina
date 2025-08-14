package org.francalderon.app.fotocabina;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.service.ArchivoService;
import org.francalderon.app.fotocabina.service.PlantillaService;
import org.francalderon.app.fotocabina.service.ServiceManager;
import org.francalderon.app.fotocabina.service.WebcamService;
import org.francalderon.app.fotocabina.utils.*;

import java.io.IOException;

public class MainViewController {
    @FXML
    Plantilla plantilla;
    EditorImagenes editorImagenes;
    ArchivoService archivoService;
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

        imageMiniPreview.imageProperty().bind(webcamService.getMiniPreview().imageProperty());
        plantillaLive.prefWidthProperty().bind(plantilla.prefWidthProperty());
        plantillaLive.prefHeightProperty().bind(plantilla.prefHeightProperty());
        plantillaLive.getChildren().add(plantilla);
    }

    @FXML
    private CheckBox openLive;

    @FXML
    private Label labelMiniPreview;

    @FXML
    private Button btnPlantilla;

    @FXML
    private Button btnTamanio;

    @FXML
    private Button btnFondo;

    @FXML
    private Button btnImprimir;

    @FXML
    private Button btnFotos;

    @FXML
    private Button btnAjustes;

    @FXML
    private Button btnCaptura;

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
        AdminVentanas.toggleLiveWindow(openLive.isSelected());
    }

    @FXML
    protected void onCapturaClick() {
        webcamService.tomarFotosConTemporizador();
    }

    @FXML
    protected void onPlantillaClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlantillaView.fxml"));
            Parent root = loader.load();

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Ventana secundaria");
            nuevaVentana.initOwner(obtenerStage());
            nuevaVentana.initModality(Modality.NONE);
            nuevaVentana.setAlwaysOnTop(true);
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.show();
        } catch (IOException ex) {
            throw  new RuntimeException();
        }
    }

    @FXML
    protected void onTamanioClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ImageControlView.fxml"));
            Parent root = loader.load();

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Ventana secundaria");
            nuevaVentana.initOwner(obtenerStage());
            nuevaVentana.initModality(Modality.NONE);
            nuevaVentana.setAlwaysOnTop(true);
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.show();
        } catch (IOException ex) {
            throw  new RuntimeException();
        }
    }

    @FXML
    protected void onImprimirClick() {

    }

    @FXML
    protected void onFotosClick() {
        AbrirCarpeta.verFotos();
    }

    @FXML
    protected void onFondoClick() {
        Stage stage = FXUtils.obternerStage(btnFondo);
        Image nuevoFondo = SelectorImagen.seleccionarImagen(stage);
        if (nuevoFondo != null) {
            plantilla.getFondo().setImage(nuevoFondo);
        } else {
            System.out.println("No se puede cargar imagen");
        }
    }

    @FXML
    protected void onAjustesClick() {

    }

    private Stage obtenerStage(){
        return (Stage) btnCaptura.getScene().getWindow();
    }
}
