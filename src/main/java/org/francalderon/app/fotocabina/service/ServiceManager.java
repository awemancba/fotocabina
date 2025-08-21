package org.francalderon.app.fotocabina.service;

import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.utils.EditorImagenes;

import java.io.IOException;

public class ServiceManager {
    private static ServiceManager instance;

    EditorImagenes editorImagenes;
    ArchivoService archivoService;
    PlantillaService plantillaService;
    Plantilla plantilla;
    WebcamService webcamService;

    private ServiceManager() throws IOException {
        editorImagenes = new EditorImagenes();
        archivoService = new ArchivoService();
        plantillaService = new PlantillaService(archivoService);
        plantilla = new Plantilla(editorImagenes, plantillaService);
        webcamService = new WebcamService(640, 480, plantilla);
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

    public ArchivoService getArchivoService() {
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
