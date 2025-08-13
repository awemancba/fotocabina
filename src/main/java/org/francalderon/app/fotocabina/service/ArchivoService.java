package org.francalderon.app.fotocabina.service;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArchivoService {

    public ArchivoService() throws IOException {
        Path dir = Paths.get(System.getProperty("user.home"), ".fotocabina");
        Files.createDirectories(dir);
        Path file = dir.resolve("config.txt");

        if (!Files.exists(file)) {
            Files.createFile(file);
        }
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
}
