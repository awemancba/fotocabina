package org.francalderon.app.fotocabina.service;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.enums.TamanioFoto;
import org.francalderon.app.fotocabina.ui.events.foto.AsignadorEventosFoto;
import org.francalderon.app.fotocabina.utils.EditorImagenes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlantillaService {
    private Plantilla plantilla;
    private EditorImagenes editorImagenes;
    private ArchivoService archivoService;



    public PlantillaService(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public void setEditorImagenes(EditorImagenes editorImagenes) {
        this.editorImagenes = editorImagenes;
    }


    public void moverArriba() {
        mover(0, -5);
        actualizarConfig();
    }

    public void moverAbajo() {
        mover(0, 5);
        actualizarConfig();
    }

    public void moverIzquierda() {
        mover(-5, 0);
        actualizarConfig();
    }

    public void moverDerecha() {
        mover(5, 0);
        actualizarConfig();
    }


    public void agrandarFoto() {
        editorImagenes.cambiarTamanio(5);
        actualizarConfig();
    }

    public void achicarFoto() {
        editorImagenes.cambiarTamanio(-5);
        actualizarConfig();
    }

    public void cambiarTam9x13() {
        plantilla.setTamanioFoto(TamanioFoto.FOTO_9x13);
        actualizarConfig();
        ajustarVentana();
    }

    public void cambiarTam10x15() {
        plantilla.setTamanioFoto(TamanioFoto.FOTO_10x15);
        actualizarConfig();
        ajustarVentana();
    }

    public void cambiarTam13x18() {
        plantilla.setTamanioFoto(TamanioFoto.FOTO_13x18);
        actualizarConfig();
        ajustarVentana();
    }


    private void actualizarConfig() {
        StringBuilder configuracion = new StringBuilder();
        int cantidadFotos = plantilla.getGaleria().size();
        configuracion.append(cantidadFotos).append("\n");

        String tamanioFoto = plantilla.getTamanioFoto().getNombre();
        configuracion.append(tamanioFoto).append("\n");

        for (StackPane fotos : plantilla.getGaleria()) {
            configuracion.append(fotos.getLayoutX()).append(",").append(fotos.getLayoutY()).append(",").append(fotos.getHeight()).append("\n");
        }
        archivoService.crearArchivo(System.getProperty("user.home") + "/.fotocabina/config.txt", configuracion.toString());
    }


    public void cargarUltimaConfig() {
        File archivo = new File(System.getProperty("user.home") + "/.fotocabina/config.txt");
        List<String> ultimaConfig = archivoService.leerArchivo(archivo);
        int cantidadFotos;

        if (ultimaConfig == null || ultimaConfig.size() < 2) {
            ultimaConfig = new ArrayList<>();
            cargarDatosDefault(ultimaConfig);
        }

        try {
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        } catch (NumberFormatException e) {
            ultimaConfig.clear();
            cargarDatosDefault(ultimaConfig);
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        }


        if (ultimaConfig.size() < 2 + cantidadFotos) {
            ultimaConfig.clear();
            cargarDatosDefault(ultimaConfig);
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        }

        List<String> configuracion = ultimaConfig.subList(0, 2);
        List<String> coordenadas = ultimaConfig.subList(2, ultimaConfig.size());

        String tamanio = configuracion.get(1);

        for (int i = 0; i < cantidadFotos; i++) {
            StackPane imagen = crearFotoDefault();
            String[] coordenada = coordenadas.get(i).split(",");

            double newX = Double.parseDouble(coordenada[0]);
            double newY = Double.parseDouble(coordenada[1]);
            double alto = Double.parseDouble(coordenada[2]);

            if (alto == 0) {
                alto = 200.0;
            }
            ((ImageView) imagen.getChildren().getFirst()).setFitHeight(alto);
            imagen.setLayoutX(newX);
            imagen.setLayoutY(newY);

            AsignadorEventosFoto.selected(imagen, i, plantilla);
            AsignadorEventosFoto.arrastrarFoto(imagen);

            plantilla.addImage(imagen);
            plantilla.getGaleria().add(imagen);

            switch (tamanio) {
                case "9x13" -> Platform.runLater(this::cambiarTam9x13);
                case "10x15" -> Platform.runLater(this::cambiarTam10x15);
                case "13x18" -> Platform.runLater(this::cambiarTam13x18);
            }
        }
    }

    private void mover(int dx, int dy) {
        plantilla.getEditorImagenes().cambiarPosicion(dx, dy);
    }

    private void ajustarVentana() {
        Stage stage = (Stage) plantilla.getScene().getWindow();
        stage.sizeToScene();
    }

    private StackPane crearFotoDefault() {
        ImageView foto = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/img/fotoDefault.jpg")).toExternalForm()));
        foto.setPreserveRatio(true);

        Label selected = new Label("Selected");
        selected.setStyle("-fx-text-fill: white;-fx-font-size: 32px;");
        selected.setVisible(false);

        StackPane contenedor = new StackPane(foto, selected);
        contenedor.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.3, 5, 5);");
        return contenedor;
    }

    private void cargarDatosDefault(List<String> ultimaConfig) {
        ultimaConfig.clear();
        ultimaConfig.add("3");
        ultimaConfig.add("10x15");
        for (int i = 0; i < 3; i++) {
            ultimaConfig.add("45.0," + (200.0 * (i + 1)) + "," + "200.0");
        }
    }
}
