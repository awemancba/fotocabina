package org.francalderon.app.fotocabina.models;

import java.util.List;

public class PlantillaDTO {
    private String tamanioPlantilla;
    private String ubicacionFondo;
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
}
