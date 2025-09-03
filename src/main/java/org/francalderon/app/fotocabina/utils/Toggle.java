package org.francalderon.app.fotocabina.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Toggle {
    public static void clase(Node node, String cssClass) {
        ObservableList<String> classes = node.getStyleClass();
        if (classes.contains(cssClass)) {
            classes.remove(cssClass);
        } else {
            classes.add(cssClass);
        }
    }
}
