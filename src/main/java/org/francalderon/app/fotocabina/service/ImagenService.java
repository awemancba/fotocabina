package org.francalderon.app.fotocabina.service;

import org.francalderon.app.fotocabina.utils.EditorImagenes;

import java.awt.image.BufferedImage;

public class ImagenService {
    public static BufferedImage recortar13to10(BufferedImage imagen){
        return EditorImagenes.recortarApectRatio(imagen,13.0,10.0);
    }
}
