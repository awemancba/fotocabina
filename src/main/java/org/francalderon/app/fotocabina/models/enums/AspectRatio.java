package org.francalderon.app.fotocabina.models.enums;

public enum AspectRatio {
    FOTO1_1(1.0, 1.0,1.0),
    FOTO4_3(4.0, 3.0, 1.33),
    FOTO16_9(16.0, 9.0,1.77);
    private final Double aspectWidth;
    private final Double aspectHeight;
    private final Double aspectRatio;


    AspectRatio(Double aspectWidth, Double aspectHeight, Double aspectRatio) {
        this.aspectWidth = aspectWidth;
        this.aspectHeight = aspectHeight;
        this.aspectRatio = aspectRatio;
    }

    public Double getAspectHeight() {
        return aspectHeight;
    }

    public Double getAspectWidth() {
        return aspectWidth;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }
}
