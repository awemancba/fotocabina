package org.francalderon.app.fotocabina.ui.events.foto;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.List;

public class activarDesactivarNodo {
    public static void textOff(List<StackPane> galeria) {
        mostrarNodo(galeria,false);
    }

    public static void textOn(List<StackPane> galeria){
        mostrarNodo(galeria,true);
    }

    private static void mostrarNodo(List<StackPane> galeria , boolean activar){
        galeria.forEach(stackPane -> stackPane.getChildren()
                .stream()
                .filter(nodo -> nodo instanceof Label)
                .findFirst()
                .ifPresent(label -> label.setVisible(activar)));
    }

}
