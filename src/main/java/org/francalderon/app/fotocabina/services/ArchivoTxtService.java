package org.francalderon.app.fotocabina.services;

import javafx.scene.layout.StackPane;
import org.francalderon.app.fotocabina.models.BaseArchivoService;
import org.francalderon.app.fotocabina.services.interfaces.ArchivoService;
import org.francalderon.app.fotocabina.utils.AdminVentanas;
import org.francalderon.app.fotocabina.utils.GuardadorArchivo;

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
        GuardadorArchivo.plantillaTxt(AdminVentanas.getPrimaryStage());

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

        crearArchivo(System.getProperty("user.home") + "/.fotocabina/config.txt", datos);
    }



}
