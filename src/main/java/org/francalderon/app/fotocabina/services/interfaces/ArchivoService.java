package org.francalderon.app.fotocabina.services.interfaces;

import java.io.File;

public interface ArchivoService<T> {
    void crearArchivo(String nombre, T contenido);
    T leerArchivo(File archivo);
    void guardarConfig();
    void actualizarConfig();
}
