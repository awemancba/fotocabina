package org.francalderon.app.fotocabina.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminVentanas {
    private static Stage ventanaVivo;
    private static Stage plantillaView;
    private static Stage imgaeControl;
    private static Stage primaryStage;

    public static void toggleLiveWindow(CheckBox checkBox) throws IOException {
        if (checkBox.isSelected()) {
            if (ventanaVivo == null || !ventanaVivo.isShowing()) {
                FXMLLoader loader = new FXMLLoader(AdminVentanas.class.getResource("/fxml/LiveView.fxml"));
                Parent root = loader.load();
                ventanaVivo = new Stage();
                ventanaVivo.setScene(new Scene(root));
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
        if (imgaeControl == null || !imgaeControl.isShowing()) {
            FXMLLoader loader = new FXMLLoader(AdminVentanas.class.getResource("/fxml/ImageControlView.fxml"));
            Parent root = loader.load();

            imgaeControl = new Stage();
            imgaeControl.setTitle("Controles Imagen");
            imgaeControl.initOwner(stage);
            imgaeControl.initModality(Modality.NONE);
            imgaeControl.setAlwaysOnTop(true);
            imgaeControl.setScene(new Scene(root));
            imgaeControl.show();
        } else {
            imgaeControl.close();
            imgaeControl = null;
        }
    }

    public static Stage getVentanaVivo() {
        return ventanaVivo;
    }

    public static  Stage getPlantillaView(){
        return plantillaView;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        AdminVentanas.primaryStage = primaryStage;
    }
}
