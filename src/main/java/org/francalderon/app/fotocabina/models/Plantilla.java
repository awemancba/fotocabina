package org.francalderon.app.fotocabina.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import org.francalderon.app.fotocabina.models.enums.TamanioFoto;
import org.francalderon.app.fotocabina.services.PlantillaService;
import org.francalderon.app.fotocabina.utils.EditorImagenes;
import org.francalderon.app.fotocabina.utils.EscaladorProporcional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Plantilla extends Pane {
    private ImageView fondo;
    private List<StackPane> galeria = new ArrayList<>();
    private int imgSelected = 0;
    private EditorImagenes editorImagenes;
    private PlantillaService plantillaService;
    private TamanioFoto tamanioFoto;
    public static final String CONFIGURACION_TXT = System.getProperty("user.home") + "/.fotocabina/config.txt";
    public static final String CONFIGURACION_JSON = System.getProperty("user.home") + "/.fotocabina/config.json";
    public static final int ALTO_BARRA_TITULO = 20;


    public Plantilla() {
        tamanioFoto = TamanioFoto.FOTO_10x15;

        Image imgFondo = new Image(Objects.requireNonNull(getClass().getResource("/img/fondoDefault.jpg")).toExternalForm());

        double alto = Screen.getPrimary().getVisualBounds().getHeight() - ALTO_BARRA_TITULO;
        double ancho = EscaladorProporcional.calcularAncho(imgFondo.getWidth(), imgFondo.getHeight(), alto).getWidth();

        System.out.println("ancho = " + ancho);
        System.out.println("alto = " + alto);

        this.fondo = new ImageView();
        this.fondo.setFitWidth(ancho);
        this.fondo.setFitHeight(alto);
        this.setPrefSize(ancho, alto);
        this.getChildren().add(this.fondo);
    }

    public EditorImagenes getEditorImagenes() {
        return editorImagenes;
    }

    public void setEditorImagenes(EditorImagenes editorImagenes) {
        this.editorImagenes = editorImagenes;
    }

    public ImageView getFondo() {
        return fondo;
    }


    public List<StackPane> getGaleria() {
        return galeria;
    }

    public void setGaleria(List<Image> fotos) {
        for (int i = 0; i < fotos.size(); i++) {
            StackPane stackPane = this.galeria.get(i);
            for (Node nodo : stackPane.getChildren()) {
                if (nodo instanceof ImageView) {
                    ((ImageView) nodo).setImage(fotos.get(i));
                }
            }
        }
    }

    public int getImgSelected() {
        return imgSelected;
    }

    public void setImgSelected(int imgSelected) {
        this.imgSelected = imgSelected;
    }

    public void setPlantillaService(PlantillaService plantillaService) {
        this.plantillaService = plantillaService;
    }

    public TamanioFoto getTamanioFoto() {
        return tamanioFoto;
    }

    public void setTamanioFoto(TamanioFoto tamanioFoto) {
        double anchoNuevo = EscaladorProporcional.calcularAncho(tamanioFoto.getAncho(), tamanioFoto.getAlto(), this.getHeight()).getWidth();
        this.fondo.setFitWidth(anchoNuevo);
        this.setPrefWidth(anchoNuevo);
        this.tamanioFoto = tamanioFoto;
    }

    public StackPane selected() {
        return this.galeria.get(imgSelected);
    }

    public void addImage(StackPane stackPane) {
        this.galeria.add(stackPane);
        this.getChildren().add(stackPane);
    }

    public void deleteImage() {
        this.getChildren().remove(selected());
        this.galeria.remove(selected());
    }
}
