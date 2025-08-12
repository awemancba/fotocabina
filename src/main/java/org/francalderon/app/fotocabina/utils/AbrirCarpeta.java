package org.francalderon.app.fotocabina.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AbrirCarpeta {
    public static void verFotos(){
        try {
            File carpeta = new File(System.getProperty("user.home") + "/.fotocabina/");
            if (carpeta.exists()) {
                Desktop.getDesktop().open(carpeta);
            } else {
                System.out.println("La carpeta no existe.");
            }
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}
