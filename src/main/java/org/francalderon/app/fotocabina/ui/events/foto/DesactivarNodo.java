package org.francalderon.app.fotocabina.ui.events.foto;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.List;

public class DesactivarNodo {
    public static void texto(List<StackPane> galeria) {
        galeria.forEach(stackPane -> stackPane.getChildren()
                .stream()
                .filter(nodo -> nodo instanceof Label)
                .findFirst()
                .ifPresent(label -> label.setVisible(false)));
    }

}
