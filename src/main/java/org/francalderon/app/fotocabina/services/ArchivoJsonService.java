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
import org.francalderon.app.fotocabina.models.ConfigDTO;
import org.francalderon.app.fotocabina.models.enums.AspectRatio;
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

public class ArchivoJsonService extends BaseArchivoService implements ArchivoService<ConfigDTO> {

    private final ObjectMapper mapper = new ObjectMapper();

    public ArchivoJsonService() throws IOException {
        super();
        Path file = BaseArchivoService.FOTOCABINA.resolve("config.json");
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    @Override
    public void crearArchivo(String nombre, ConfigDTO contenido) {
        try {
            mapper.writeValue(new File(nombre), contenido);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ConfigDTO leerArchivo(File archivo) {
        try {
            return mapper.readValue(archivo, ConfigDTO.class);
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
        ConfigDTO configDTO = new ConfigDTO();
        List<FotoDTO> fotosDTO = new ArrayList<>();
        List<StackPane> fotos = plantilla.getGaleria();

        configDTO.setTamanioPlantilla(plantilla.getTamanioFoto().getNombre());
        configDTO.setUbicacionFondo(plantilla.getFondo().getImage().getUrl());
        configDTO.setAspectRatio(plantillaService.getWebcamService().getAspectRatio());

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
        configDTO.setFotos(fotosDTO);

        crearArchivo(Plantilla.CONFIGURACION_JSON, configDTO);

    }

    @Override
    public void cargarConfig(File archivo) {
        ConfigDTO configDTO;

        if (archivo.length() == 0) {
            configDTO = new ConfigDTO();
            configDTO.setUbicacionFondo(Objects.requireNonNull(getClass().getResource("/img/fondoDefault.jpg")).toExternalForm());
            configDTO.setTamanioPlantilla("10x15");
            configDTO.setAspectRatio(AspectRatio.FOTO4_3);

            List<FotoDTO> fotosDTO = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                FotoDTO foto = new FotoDTO();
                foto.setAlto(200);
                foto.setX(45);
                foto.setY(200 * (i + 1));
                fotosDTO.add(foto);
            }
            configDTO.setFotos(fotosDTO);
        } else {
            configDTO = leerArchivo(archivo);
        }

        AspectRatio aspectRatio = configDTO.getAspectRatio();
        String tamanio = configDTO.getTamanioPlantilla();
        String fondo = configDTO.getUbicacionFondo();
        List<FotoDTO> fotos = configDTO.getFotos();

        webcamService.setAspectRatio(aspectRatio);
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
            ((ImageView) foto.getChildren().getFirst()).setFitWidth(alto * (aspectRatio.getAspectRatio()));

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
