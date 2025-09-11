package org.francalderon.app.fotocabina.services;

import org.francalderon.app.fotocabina.models.BaseArchivoService;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.models.ConfigDTO;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;
import org.francalderon.app.fotocabina.utils.EditorImagenes;

import java.io.IOException;

public class ServiceManager {
    private static ServiceManager instance;

    private final EditorImagenes editorImagenes;
    private final ArchivoService<ConfigDTO> archivoService;
    private final PlantillaService plantillaService;
    private final Plantilla plantilla;
    private final WebcamService webcamService;

    private ServiceManager() throws IOException {
        plantillaService = new PlantillaService();
        plantilla = new Plantilla();
        editorImagenes = new EditorImagenes();
        archivoService = new ArchivoJsonService();
        webcamService = new WebcamService(640, 480, plantilla);

        editorImagenes.setPlantilla(plantilla);
        ((BaseArchivoService) archivoService).setPlantilla(plantilla);
        ((BaseArchivoService) archivoService).setPlantillaService(plantillaService);
        ((BaseArchivoService) archivoService).setWebcamService(webcamService);
        plantillaService.setPlantilla(plantilla);
        plantillaService.setEditorImagenes(editorImagenes);
        plantillaService.setArchivoService(archivoService);
        plantillaService.setWebcamService(webcamService);
        plantilla.setPlantillaService(plantillaService);
        plantilla.setEditorImagenes(editorImagenes);

    }

    public void iniciarServicios() {
        webcamService.start();
    }

    public void detenerServicios() {
        webcamService.stop();
    }

    public static ServiceManager getInstance() throws IOException {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public EditorImagenes getEditorImagenes() {
        return editorImagenes;
    }

    public ArchivoService<ConfigDTO> getArchivoService() {
        return archivoService;
    }

    public PlantillaService getPlantillaService() {
        return plantillaService;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public WebcamService getWebcamService() {
        return webcamService;
    }

}
