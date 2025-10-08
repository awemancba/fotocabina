package org.francalderon.app.fotocabina.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminVentanas {
    private static Stage ventanaVivo;
    private static Stage plantillaView;
    private static Stage imageControl;
    private static Stage mainViewController;

    public static void toggleLiveWindow(CheckBox checkBox) throws IOException {
        if (checkBox.isSelected()) {
            if (ventanaVivo == null || !ventanaVivo.isShowing()) {
                FXMLLoader loader = new FXMLLoader(AdminVentanas.class.getResource("/fxml/LiveView.fxml"));
                Parent root = loader.load();
                Rectangle2D monitor = Screen.getPrimary().getVisualBounds();
                ventanaVivo = new Stage();
                ventanaVivo.setScene(new Scene(root));
                ventanaVivo.setWidth(monitor.getWidth());
                ventanaVivo.setTitle("Vivo");
                ventanaVivo.setAlwaysOnTop(true);
                ventanaVivo.setOnCloseRequest(e -> {
                    ventanaVivo = null;
                    checkBox.setSelected(false);
                });
                ventanaVivo.show();
            }
        } else {
            if (ventanaVivo != null) {
                ventanaVivo.close();
                ventanaVivo = null;
            }
        }
    }

    public static void plantillaView(Stage stage) throws IOException {
        if (plantillaView == null || !plantillaView.isShowing()) {
            FXMLLoader loader = new FXMLLoader(AdminVentanas.class.getResource("/fxml/PlantillaView.fxml"));
            Parent root = loader.load();

            plantillaView = new Stage();
            plantillaView.setTitle("Ventana secundaria");
            plantillaView.initOwner(stage);
            plantillaView.initModality(Modality.NONE);
            plantillaView.setAlwaysOnTop(true);
            plantillaView.setScene(new Scene(root));
            plantillaView.show();
        } else {
            plantillaView.close();
            plantillaView = null;
        }

    }

    public static void imageControlView(Stage stage) throws IOException {
        if (imageControl == null || !imageControl.isShowing()) {
            FXMLLoader loader = new FXMLLoader(AdminVentanas.class.getResource("/fxml/ImageControlView.fxml"));
            Parent root = loader.load();

            imageControl = new Stage();
            imageControl.setTitle("Controles Imagen");
            imageControl.initOwner(stage);
            imageControl.initModality(Modality.NONE);
            imageControl.setAlwaysOnTop(true);
            imageControl.setScene(new Scene(root));
            imageControl.show();
        } else {
            imageControl.close();
            imageControl = null;
        }
    }

    public static Stage getVentanaVivo() {
        return ventanaVivo;
    }

    public static  Stage getPlantillaView(){
        return plantillaView;
    }

    public static Stage getMainViewController() {
        return mainViewController;
    }

    public static void setMainViewController(Stage mainViewController) {
        AdminVentanas.mainViewController = mainViewController;
    }
}
