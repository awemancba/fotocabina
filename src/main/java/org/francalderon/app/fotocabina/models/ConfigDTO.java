package org.francalderon.app.fotocabina.models;

import org.francalderon.app.fotocabina.models.enums.AspectRatio;

import java.util.List;

public class ConfigDTO {
    private String tamanioPlantilla;
    private String ubicacionFondo;
    private AspectRatio aspectRatio;
    private int tiempo;
    private String command;
    private String ubicacionCapturador;
    private List<FotoDTO> fotos;

    public ConfigDTO() {
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

    public String getUbicacionCapturador() {
        return ubicacionCapturador;
    }

    public void setUbicacionCapturador(String ubicacionCapturador) {
        this.ubicacionCapturador = ubicacionCapturador;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
