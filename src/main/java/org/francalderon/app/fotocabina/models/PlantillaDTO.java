package org.francalderon.app.fotocabina.models;

import org.francalderon.app.fotocabina.models.enums.AspectRatio;

import java.util.List;

public class PlantillaDTO {
    private String tamanioPlantilla;
    private String ubicacionFondo;
    private AspectRatio aspectRatio;
    private List<FotoDTO> fotos;

    public PlantillaDTO() {
    }

    public String getTamanioPlantilla() {
        return tamanioPlantilla;
    }

    public void setTamanioPlantilla(String tamanioPlantilla) {
        this.tamanioPlantilla = tamanioPlantilla;
    }

    public String getUbicacionFondo() {
        return ubicacionFondo;
    }

    public void setUbicacionFondo(String ubicacionFondo) {
        this.ubicacionFondo = ubicacionFondo;
    }

    public List<FotoDTO> getFotos() {
        return fotos;
    }

    public void setFotos(List<FotoDTO> fotos) {
        this.fotos = fotos;
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public AspectRatio getAspectRatio() {
        return aspectRatio;
    }
}
