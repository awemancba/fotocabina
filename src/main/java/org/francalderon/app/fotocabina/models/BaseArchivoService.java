package org.francalderon.app.fotocabina.models;

import javafx.stage.Stage;
import org.francalderon.app.fotocabina.services.PlantillaService;
import org.francalderon.app.fotocabina.services.WebcamService;
import org.francalderon.app.fotocabina.utils.SelectorArchivo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class BaseArchivoService {
    protected Plantilla plantilla;
    protected PlantillaService plantillaService;
    protected WebcamService webcamService;
    public static final Path RESOURCES = Paths.get(System.getProperty("user.home"), ".fotocabina", "resources");
    public static final Path FOTOCABINA = Paths.get(System.getProperty("user.home"), ".fotocabina");

    public BaseArchivoService() throws IOException {
        Files.createDirectories(FOTOCABINA);
        Files.createDirectories(RESOURCES);
    }

    public static void copiarArchivo(Path origen, Path destino) {
        try {
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo Copiado exitosamente");
        } catch (IOException e) {
            System.out.println("Error al copiar el archivo" + e.getMessage());
        }
    }

    public static String copyToResources(Stage stage) {
        File archivoSeleccionado = SelectorArchivo.seleccionarImagenFile(stage);
        Path origen;
        Path destino;
        if (archivoSeleccionado != null) {
            origen = archivoSeleccionado.toPath();
            destino = generarDestinoConRenombre(RESOURCES.resolve(archivoSeleccionado.getName()));

            copiarArchivo(origen, destino);
            return destino.toUri().toString();
        } else {
            return null;
        }
    }

    public static Path generarDestinoConRenombre(Path destinoOriginal) {
        Path destino = destinoOriginal;
        int contador = 1;

        while (Files.exists(destino)) {
            String nombre = destinoOriginal.getFileName().toString();
            String base = nombre.contains(".") ? nombre.substring(0, nombre.lastIndexOf(".")) : nombre;
            String extension = nombre.contains(".") ? nombre.substring(nombre.lastIndexOf(".")) : "";

            String nuevoNombre = base + "_" + contador + extension;

            destino = destinoOriginal.getParent().resolve(nuevoNombre);
            contador++;
        }
        return destino;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public PlantillaService getPlantillaService() {
        return plantillaService;
    }

    public void setPlantillaService(PlantillaService plantillaService) {
        this.plantillaService = plantillaService;
    }

    public void setWebcamService(WebcamService webcamService) {
        this.webcamService = webcamService;
    }
}
