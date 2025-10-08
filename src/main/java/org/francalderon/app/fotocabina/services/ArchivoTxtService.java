package org.francalderon.app.fotocabina.services;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.BaseArchivoService;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;
import org.francalderon.app.fotocabina.ui.events.foto.AsignadorEventosFoto;
import org.francalderon.app.fotocabina.utils.AdminVentanas;
import org.francalderon.app.fotocabina.utils.GuardadorArchivo;
import org.francalderon.app.fotocabina.utils.SelectorArchivo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArchivoTxtService extends BaseArchivoService implements ArchivoService<List<String>> {
    public ArchivoTxtService() throws IOException {
        super();
        Path file = BaseArchivoService.FOTOCABINA.resolve("config.txt");
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
    }

    @Override
    public void crearArchivo(String nombre, List<String> texto) {
        File archivo = new File(nombre);
        try (PrintWriter buffer = new PrintWriter(new FileWriter(archivo))) {
            for (String linea : texto) {
                buffer.println(linea);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> leerArchivo(File nombre) {
        List<String> ultimaConfig = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nombre))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                ultimaConfig.add(linea);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return ultimaConfig;
    }

    public void guardarConfig() {
        GuardadorArchivo.plantillaTxt(AdminVentanas.getMainViewController());

    }

    @Override
    public void actualizarConfig() {
        StringBuilder configuracion = new StringBuilder();
        int cantidadFotos = plantilla.getGaleria().size();
        configuracion.append(cantidadFotos).append("\n");

        String tamanioFoto = plantilla.getTamanioFoto().getNombre();
        configuracion.append(tamanioFoto).append("\n");

        String urlFondo = plantilla.getFondo().getImage().getUrl();
        configuracion.append(urlFondo).append("\n");

        for (StackPane fotos : plantilla.getGaleria()) {
            configuracion.append(fotos.getLayoutX()).append(",").append(fotos.getLayoutY()).append(",").append(fotos.getHeight()).append("\n");
        }

        List<String> datos = Arrays.asList(configuracion.toString().split("\n"));

        crearArchivo(Plantilla.CONFIGURACION_TXT, datos);
    }

    @Override
    public void cargarConfig(File archivo) {
        List<String> ultimaConfig = leerArchivo(archivo);
        int cantidadFotos;
        Image fondo;

        if (ultimaConfig == null || ultimaConfig.size() < 3) {
            ultimaConfig = new ArrayList<>();
            plantillaService.cargarDatosDefault(ultimaConfig);
        }

        try {
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        } catch (NumberFormatException e) {
            ultimaConfig.clear();
            plantillaService.cargarDatosDefault(ultimaConfig);
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        }

        if (ultimaConfig.size() < 3 + cantidadFotos) {
            ultimaConfig.clear();
            plantillaService.cargarDatosDefault(ultimaConfig);
            cantidadFotos = Integer.parseInt(ultimaConfig.getFirst());
        }

        List<String> configuracion = ultimaConfig.subList(0, 3);
        List<String> coordenadas = ultimaConfig.subList(3, ultimaConfig.size());

        String tamanio = configuracion.get(1);
        String urlFondo = configuracion.get(2);
        fondo = new Image(urlFondo);

        plantilla.getFondo().setImage(fondo);

        for (int i = 0; i < cantidadFotos; i++) {
            StackPane imagen = plantillaService.crearFotoDefault(i);
            String[] coordenada = coordenadas.get(i).split(",");

            double newX = Double.parseDouble(coordenada[0]);
            double newY = Double.parseDouble(coordenada[1]);
            double alto = Double.parseDouble(coordenada[2]);

            if (alto == 0) {
                alto = 200.0;
            }
            ((ImageView) imagen.getChildren().getFirst()).setFitHeight(alto);
            imagen.setLayoutX(newX);
            imagen.setLayoutY(newY);

            plantilla.addImage(imagen);

            AsignadorEventosFoto.selected(imagen, i, plantilla);
            AsignadorEventosFoto.arrastrarFoto(plantillaService, imagen);
        }

        switch (tamanio) {
            case "9x13" -> Platform.runLater(() -> plantillaService.cambiarTam9x13());
            case "10x15" -> Platform.runLater(() -> plantillaService.cambiarTam10x15());
            case "13x18" -> Platform.runLater(() -> plantillaService.cambiarTam13x18());
        }
    }

    @Override
    public void cargarPlantilla() {
        File config = SelectorArchivo.seleccionarConfigFile(AdminVentanas.getMainViewController());
        if (config!=null){
            plantillaService.eliminarComponentes();
            cargarConfig(config);
            actualizarConfig();
        }else {
            System.out.println("El archivo no se ha cargado");
        }
    }
}
