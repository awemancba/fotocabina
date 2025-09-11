package org.francalderon.app.fotocabina;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.ConfigDTO;
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
    ArchivoService<ConfigDTO> archivoService;
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

        controles.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight() - Plantilla.ALTO_BARRA_TITULO);


        Platform.runLater(() -> {
            aplicarEstilos();
            ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                aplicarEstilos();
            };

            obtenerStage().widthProperty().addListener(stageSizeListener);
            obtenerStage().heightProperty().addListener(stageSizeListener);


            Scene scene = btnPlantilla.getScene();
            Stage stage = (Stage) scene.getWindow();
            EliminarComponente.foto(stage, scene, plantillaService);
            obtenerStage().setOnCloseRequest(e -> {
                webcamService.stop();
                if (AdminVentanas.getVentanaVivo() != null) {
                    AdminVentanas.getVentanaVivo().close();
                }
            });
        });

    }

    @FXML
    private void aplicarEstilos() {
        for (Node nodo : botones.getChildren()) {
            double altoStage = obtenerStage().getHeight();
            if (altoStage < 800) {
                nodo.getStyleClass().clear();
                nodo.getStyleClass().add("boton-control-menor");
                ((Button) nodo).getGraphic().getStyleClass().clear();
                ((Button) nodo).getGraphic().getStyleClass().add("img-boton-menor");
            } else if (altoStage < 900) {
                nodo.getStyleClass().clear();
                nodo.getStyleClass().add("boton-control-medio");
                ((Button) nodo).getGraphic().getStyleClass().clear();
                ((Button) nodo).getGraphic().getStyleClass().add("img-boton-medio");
            } else {
                nodo.getStyleClass().clear();
                nodo.getStyleClass().add("boton-control");
                ((Button) nodo).getGraphic().getStyleClass().clear();
                ((Button) nodo).getGraphic().getStyleClass().add("img-boton");
            }
            nodo.getStyleClass().add("color-primario");
        }
    }

    @FXML
    private CheckBox modoEspejo;

    @FXML
    private CheckBox hidePreview;

    @FXML
    private GridPane botones;

    @FXML
    private CheckBox openLive;

    @FXML
    private Button btnVerFotos;

    @FXML
    private Button btnCaptura;

    @FXML
    private Button btnPlantilla;

    @FXML
    private Button btnFotos;

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
    private TextField tiempoTemp;

    //Botones

    @FXML
    protected void onOpenLiveClick() throws IOException {
        AdminVentanas.toggleLiveWindow(openLive);
    }

    @FXML
    protected void onCapturaClick() {
        if (AdminVentanas.getVentanaVivo() != null){
            webcamService.tomarFotosConTemporizador();
        } else {
            System.out.println("Por favor abra la ventana en vivo");
        }

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
    protected void onCargarPlantillaClick() {
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
        if (nuevoIcono != null) {
            webcamService.getIcono().setImage(nuevoIcono);
        } else {
            System.out.println("No se puede cargar la imagen");
        }
    }

    @FXML
    protected void onEstirarVivoClick(){

    }

    @FXML
    protected void onHidePreviewClick(){
        if (hidePreview.isSelected()){
            imageMiniPreview.imageProperty().unbind();
            imageMiniPreview.setImage(null);
            imageMiniPreview.setVisible(false);
            imageMiniPreview.setManaged(false);
            webcamService.getMiniPreview().setVisible(false);
        } else {
            imageMiniPreview.imageProperty().bind(webcamService.getMiniPreview().imageProperty());
            imageMiniPreview.setVisible(true);
            imageMiniPreview.setManaged(true);
            webcamService.getMiniPreview().setVisible(true);
        }
    }

    @FXML
    protected void onTiempoTemp(){
        webcamService.setTiempo(Integer.parseInt(tiempoTemp.getText()));
    }

    @FXML
    protected void onModoEspejoClick(){
        webcamService.setModoEspejo(modoEspejo.isSelected());
    }

    private Stage obtenerStage() {
        return (Stage) btnCaptura.getScene().getWindow();
    }
}
