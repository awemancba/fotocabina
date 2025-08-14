package org.francalderon.app.fotocabina.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminVentanas {
    private static Stage ventanaVivo;

    public static void toggleLiveWindow(Boolean abrir) throws IOException {
        if (abrir) {
            if (ventanaVivo == null || !ventanaVivo.isShowing()) {
                FXMLLoader loader = new FXMLLoader(AdminVentanas.class.getResource("/fxml/LiveView.fxml"));
                Parent root = loader.load();
                ventanaVivo = new Stage();
                ventanaVivo.setScene(new Scene(root));
                ventanaVivo.setTitle("Vivo");
                ventanaVivo.setAlwaysOnTop(true);
                ventanaVivo.setOnCloseRequest(e -> ventanaVivo = null);
                ventanaVivo.show();
            }
        } else {
            if (ventanaVivo != null) {
                ventanaVivo.close();
                ventanaVivo = null;
            }
        }
    }
}
