package org.francalderon.app.fotocabina.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.BaseArchivoService;
import org.francalderon.app.fotocabina.models.FotoDTO;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.PlantillaDTO;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ArchivoJsonService<T> extends BaseArchivoService implements ArchivoService<T> {

    private final Class<T> tipo;
    private final ObjectMapper mapper = new ObjectMapper();
    private Plantilla plantilla;

    public ArchivoJsonService(Class<T> tipo) throws IOException {
        super();
        this.tipo = tipo;
        Path file = BaseArchivoService.FOTOCABINA.resolve("config.json");
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        plantilla = ServiceManager.getInstance().getPlantilla();
    }

    @Override
    public void crearArchivo(String nombre, T contenido) {
        try {
            mapper.writeValue(new File(nombre), contenido);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T leerArchivo(File archivo) {
        try {
            return mapper.readValue(archivo, tipo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void guardarConfig() {

    }

    @Override
    public void actualizarConfig() {
        PlantillaDTO plantillaDTO = new PlantillaDTO();
        List<StackPane> fotos = plantilla.getGaleria();

        plantillaDTO.setCantidadFotos(fotos.size());
        plantillaDTO.setTamanioPlantilla(plantilla.getTamanioFoto().getNombre());
        plantillaDTO.setUbicacionFondo(plantilla.getFondo().getImage().getUrl());

        for (StackPane foto : fotos) {
            for (Node nodo : foto.getChildren()) {
                if (nodo instanceof ImageView) {
                    FotoDTO fotoDTO = new FotoDTO();
                    fotoDTO.setAlto(((ImageView) nodo).getFitHeight());
                    fotoDTO.setAncho(((ImageView) nodo).getFitWidth());
                    fotoDTO.setX(foto.getLayoutX());
                    fotoDTO.setY(foto.getLayoutY());
                    plantillaDTO.getFotos().add(fotoDTO);
                }
            }
        }

    }

}
