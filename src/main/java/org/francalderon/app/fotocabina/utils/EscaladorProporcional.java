package org.francalderon.app.fotocabina.utils;

import java.awt.*;

public class EscaladorProporcional {
    public static Dimension calcularAncho(double anchoOriginal, double altoOriginal, double altoPreview) {
        double escala = altoPreview / altoOriginal;
        int anchoPreview = (int) (anchoOriginal * escala);
        return new Dimension(anchoPreview, (int) altoPreview);
    }

}
