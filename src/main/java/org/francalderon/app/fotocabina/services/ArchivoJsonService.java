package org.francalderon.app.fotocabina.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.BaseArchivoService;
import org.francalderon.app.fotocabina.models.FotoDTO;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.PlantillaDTO;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;
import org.francalderon.app.fotocabina.ui.events.foto.AsignadorEventosFoto;
import org.francalderon.app.fotocabina.utils.AdminVentanas;
import org.francalderon.app.fotocabina.utils.GuardadorArchivo;
import org.francalderon.app.fotocabina.utils.SelectorArchivo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArchivoJsonService extends BaseArchivoService implements ArchivoService<PlantillaDTO> {

    private final ObjectMapper mapper = new ObjectMapper();

    public ArchivoJsonService() throws IOException {
        super();
        Path file = BaseArchivoService.FOTOCABINA.resolve("config.json");
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    @Override
    public void crearArchivo(String nombre, PlantillaDTO contenido) {
        try {
            mapper.writeValue(new File(nombre), contenido);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlantillaDTO leerArchivo(File archivo) {
        try {
            return mapper.readValue(archivo, PlantillaDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void guardarConfig() {
        GuardadorArchivo.plantillaJson(AdminVentanas.getPrimaryStage());
    }

    @Override
    public void actualizarConfig() {
        PlantillaDTO plantillaDTO = new PlantillaDTO();
        List<FotoDTO> fotosDTO = new ArrayList<>();
        List<StackPane> fotos = plantilla.getGaleria();

        plantillaDTO.setTamanioPlantilla(plantilla.getTamanioFoto().getNombre());
        plantillaDTO.setUbicacionFondo(plantilla.getFondo().getImage().getUrl());

        for (StackPane foto : fotos) {
            for (Node nodo : foto.getChildren()) {
                if (nodo instanceof ImageView) {
                    FotoDTO fotoDTO = new FotoDTO();
                    fotoDTO.setAlto(((ImageView) nodo).getFitHeight());
                    fotoDTO.setX(foto.getLayoutX());
                    fotoDTO.setY(foto.getLayoutY());
                    fotosDTO.add(fotoDTO);
                }
            }
        }
        plantillaDTO.setFotos(fotosDTO);

        crearArchivo(Plantilla.CONFIGURACION_JSON, plantillaDTO);

    }

    @Override
    public void cargarConfig(File archivo) {
        PlantillaDTO plantillaDTO;

        if (archivo.length() == 0) {
            plantillaDTO = new PlantillaDTO();
            plantillaDTO.setUbicacionFondo(Objects.requireNonNull(getClass().getResource("/img/fondoDefault.jpg")).toExternalForm());
            plantillaDTO.setTamanioPlantilla("10x15");

            List<FotoDTO> fotosDTO = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                FotoDTO foto = new FotoDTO();
                foto.setAlto(200);
                foto.setX(45);
                foto.setY(200 * (i + 1));
                fotosDTO.add(foto);
            }
            plantillaDTO.setFotos(fotosDTO);
        } else {
            plantillaDTO = leerArchivo(archivo);
        }

        String tamanio = plantillaDTO.getTamanioPlantilla();
        String fondo = plantillaDTO.getUbicacionFondo();
        List<FotoDTO> fotos = plantillaDTO.getFotos();

        plantilla.getFondo().setImage(new Image(fondo));
        switch (tamanio) {
            case "9x13" -> Platform.runLater(() -> plantillaService.cambiarTam9x13());
            case "10x15" -> Platform.runLater(() -> plantillaService.cambiarTam10x15());
            case "13x18" -> Platform.runLater(() -> plantillaService.cambiarTam13x18());
        }

        for (int i = 0; i < fotos.size(); i++) {
            FotoDTO fotoDTO = fotos.get(i);
            double alto = fotoDTO.getAlto();
            double x = fotoDTO.getX();
            double y = fotoDTO.getY();

            StackPane foto = plantillaService.crearFotoDefault(i);
            foto.setLayoutX(x);
            foto.setLayoutY(y);
            ((ImageView) foto.getChildren().getFirst()).setFitHeight(alto);

            plantilla.addImage(foto);
            AsignadorEventosFoto.selected(foto, i, plantilla);
            AsignadorEventosFoto.arrastrarFoto(plantillaService, foto);
        }

    }

    @Override
    public void cargarPlantilla() {
        File config = SelectorArchivo.seleccionarConfigJson(AdminVentanas.getPrimaryStage());
        if (config != null) {
            plantillaService.eliminarComponentes();
            cargarConfig(config);
            actualizarConfig();
        } else {
            System.out.println("El archivo no se cargo correctamente");
        }
    }
}
