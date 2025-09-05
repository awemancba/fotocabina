package org.francalderon.app.fotocabina.models.enums;

public enum TamanioFoto {
    FOTO_9x13(1063, 1535,"9x13"),
    FOTO_10x15(1200, 1800,"10x15"),
    FOTO_13x18(1535, 2126,"13x18");

    private final int ancho;
    private final int alto;
    private final String nombre;

    TamanioFoto(int ancho, int alto,String nombre) {
        this.ancho = ancho;
        this.alto = alto;
        this.nombre = nombre;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String getNombre(){
        return nombre;
    }
}
