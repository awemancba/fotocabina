package org.francalderon.app.fotocabina.services;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.enums.TamanioFoto;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;
import org.francalderon.app.fotocabina.ui.events.foto.AsignadorEventosFoto;
import org.francalderon.app.fotocabina.utils.AdminVentanas;
import org.francalderon.app.fotocabina.utils.EditorImagenes;
import org.francalderon.app.fotocabina.utils.SelectorArchivo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlantillaService {
    private Plantilla plantilla;
    private EditorImagenes editorImagenes;
    private ArchivoService<List<String>> archivoTxtService;


    public PlantillaService() {
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public void setEditorImagenes(EditorImagenes editorImagenes) {
        this.editorImagenes = editorImagenes;
    }

    public void setArchivoTxtService(ArchivoService<List<String>> archivoTxtService) {
        this.archivoTxtService = archivoTxtService;
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

    public void moverTodosArriba() {
        moverTodos(0, -5);
    }

    public void moverTodosAbajo() {
        moverTodos(0, 5);
    }

    public void moverTodosIzquierda() {
        moverTodos(-5, 0);
    }

    public void moverTodosDerecha() {
        moverTodos(5, 0);
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

    public void agregarFoto() {
        int numeroFoto = plantilla.getGaleria().size();
        StackPane nuevaFoto = crearFotoDefault(numeroFoto);
        AsignadorEventosFoto.selected(nuevaFoto, numeroFoto, plantilla);
        AsignadorEventosFoto.arrastrarFoto(this, nuevaFoto);
        plantilla.addImage(nuevaFoto);
        actualizarConfig();
    }

    public void removerFoto() {
        plantilla.deleteImage();
        actualizarConfig();
        eliminarComponentes();
        File archivo = new File(Plantilla.CONFIGURACION_TXT);
        cargarConfig(archivo);
    }

    public void cambiarFondo() {
        String urlImage = ArchivoTxtService.copyToResources(AdminVentanas.getPlantillaView());
        Image nuevoFondo = new Image(urlImage);
        plantilla.getFondo().setImage(nuevoFondo);
        actualizarConfig();
    }

    public void cargarPlantilla() {
        File config = SelectorArchivo.seleccionarConfigFile(AdminVentanas.getPrimaryStage());
        if (config!=null){
            eliminarComponentes();
            cargarConfig(config);
            actualizarConfig();
        }else {
            System.out.println("El archivo no se ha cargado");
        }
    }

    public void actualizarConfig() {
        archivoTxtService.actualizarConfig();
    }

    public void cargarConfig(File archivo) {
        List<String> ultimaConfig = archivoTxtService.leerArchivo(archivo);
        int cantidadFotos;
        Image fondo;

        if (ultimaConfig == null || ultimaConfig.size() < 3) {
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

        if (ultimaConfig.size() < 3 + cantidadFotos) {
            ultimaConfig.clear();
            cargarDatosDefault(ultimaConfig);
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        }

        List<String> configuracion = ultimaConfig.subList(0, 3);
        List<String> coordenadas = ultimaConfig.subList(3, ultimaConfig.size());

        String tamanio = configuracion.get(1);
        String urlFondo = configuracion.get(2);
        fondo = new Image(urlFondo);

        plantilla.getFondo().setImage(fondo);

        for (int i = 0; i < cantidadFotos; i++) {
            StackPane imagen = crearFotoDefault(i);
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

            plantilla.addImage(imagen);

            AsignadorEventosFoto.selected(imagen, i, plantilla);
            AsignadorEventosFoto.arrastrarFoto(this, imagen);


        }
        switch (tamanio) {
            case "9x13" -> Platform.runLater(this::cambiarTam9x13);
            case "10x15" -> Platform.runLater(this::cambiarTam10x15);
            case "13x18" -> Platform.runLater(this::cambiarTam13x18);
        }
    }

    private void mover(int dx, int dy) {
        plantilla.getEditorImagenes().cambiarPosicion(dx, dy);
    }

    private void moverTodos(int dx, int dy) {
        for (int i = 0; i < plantilla.getGaleria().size(); i++) {
            plantilla.setImgSelected(i);
            plantilla.getEditorImagenes().cambiarPosicion(dx, dy);
        }
    }


    private void ajustarVentana() {
        Stage stage = (Stage) plantilla.getScene().getWindow();
        stage.sizeToScene();
    }

    private StackPane crearFotoDefault(int numeroImagen) {
        ImageView foto = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/img/fotoDefault.jpg")).toExternalForm()));
        foto.setFitHeight(200);
        foto.setPreserveRatio(true);

        Label selected = new Label(String.valueOf(numeroImagen + 1));
        selected.setStyle("-fx-text-fill: white;-fx-font-size: 32px;-fx-text-fill: rgba(255,255,255,0.5)");

        StackPane contenedor = new StackPane(foto, selected);
        contenedor.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.3, 5, 5);");
        return contenedor;
    }

    private void cargarDatosDefault(List<String> ultimaConfig) {
        ultimaConfig.clear();
        ultimaConfig.add("3");
        ultimaConfig.add("10x15");
        ultimaConfig.add(Objects.requireNonNull(getClass().getResource("/img/fondoDefault.jpg")).toExternalForm());
        for (int i = 0; i < 3; i++) {
            ultimaConfig.add("45.0," + (200.0 * (i + 1)) + "," + "200.0");
        }
    }

    private void eliminarComponentes() {
        List<Node> componentes = plantilla.getChildren();
        componentes.removeIf(node -> node instanceof Label || node instanceof StackPane);
        plantilla.getGaleria().clear();

    }
}
