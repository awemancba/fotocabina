package org.francalderon.app.fotocabina.models;

import java.util.List;

public class PlantillaDTO {
    private int cantidadFotos;
    private String tamanioPlantilla;
    private String ubicacionFondo;
    private List<FotoDTO> fotos;

    public PlantillaDTO() {
    }

    public int getCantidadFotos() {
        return cantidadFotos;
    }

    public void setCantidadFotos(int cantidadFotos) {
        this.cantidadFotos = cantidadFotos;
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
