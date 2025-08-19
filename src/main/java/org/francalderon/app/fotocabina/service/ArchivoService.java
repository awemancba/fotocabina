package org.francalderon.app.fotocabina.service;


import javafx.stage.Stage;
import org.francalderon.app.fotocabina.utils.SelectorImagen;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ArchivoService {

    public static final Path RESOURCES = Paths.get(System.getProperty("user.home"), ".fotocabina", "resources");

    public ArchivoService() throws IOException {
        Path dir = Paths.get(System.getProperty("user.home"), ".fotocabina");
        Files.createDirectories(dir);
        Path file = dir.resolve("config.txt");

        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        Path dirTemplates = Paths.get(System.getProperty("user.home"), ".fotocabina", "resources");
        Files.createDirectories(dirTemplates);
    }

    public void crearArchivo(String nombre, String texto) {
        File archivo = new File(nombre);
        try (PrintWriter buffer = new PrintWriter(new FileWriter(archivo))) {
            buffer.print(texto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static String copyToResources(Stage stage) {
        File archivoSeleccionado = SelectorImagen.seleccionarImagenFile(stage);

        Path origen = archivoSeleccionado.toPath();
        Path destino = generarDestinoConRenombre(RESOURCES.resolve(archivoSeleccionado.getName()));

        copiarArchivo(origen, destino);

        return destino.toUri().toString();
    }

    public static void copiarArchivo(Path origen, Path destino) {
        try {
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo Copiado exitosamente");
        } catch (IOException e) {
            System.out.println("Error al copiar el archivo" + e.getMessage());
        }
    }

    public static Path generarDestinoConRenombre(Path destinoOriginal) {
        Path destino = destinoOriginal;
        int contador = 1;

        while (Files.exists(destino)) {
            String nombre = destinoOriginal.getFileName().toString();
            String base = nombre.contains(".") ? nombre.substring(0, nombre.lastIndexOf(".")) : nombre;
            String extension = nombre.contains(".") ? nombre.substring(nombre.lastIndexOf(".")) : "";

            String nuevoNombre = base + "_" + contador + extension;

            destino = destinoOriginal.getParent().resolve(nuevoNombre);
            contador++;
        }
        return destino;
    }
}
