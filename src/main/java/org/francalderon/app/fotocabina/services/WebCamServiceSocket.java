package org.francalderon.app.fotocabina.services;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.francalderon.app.fotocabina.MainViewController;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.ui.events.foto.activarDesactivarNodo;
import org.francalderon.app.fotocabina.utils.ExportarPlantilla;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class WebCamServiceSocket {
    private final int port;
    private ImageView imageView;
    private PrintWriter commandWriter;
    private Plantilla plantilla;
    private ServerSocket serverSocket;
    private Thread serverThread;

    public WebCamServiceSocket(int port) {
        this.port = port;
    }

    public void startServer() {
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Servidor iniciado, escuchando en el puerto " + port);

                // Bucle principal del servidor, siempre activo para aceptar clientes.
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("------------------------------------------");
                    System.out.println("Servidor esperando nueva conexión...");

                    Socket clientSocket = serverSocket.accept(); // Esta llamada bloquea hasta que un cliente se conecta.
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    handleClient(clientSocket);

                    System.out.println("El cliente se ha desconectado. El servidor vuelve a esperar.");
                }

            } catch (SocketException e) {
                // Esta excepción es normal cuando cerramos el servidor con stopServer()
                System.out.println("Servidor detenido.");
            } catch (Exception e) {
                System.err.println("Error fatal en el hilo del servidor: " + e.getMessage());
                throw new RuntimeException();
            }
        });
        serverThread.start();
    }



    private void handleClient(Socket clientSocket) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {
            this.commandWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            List<Image> fotos = new ArrayList<>();
            int contador = 1;
            ((ArchivoJsonService) ServiceManager.getInstance().getArchivoService()).enviarConfig(MainViewController.ESPERA);

            // Bucle para leer los datos de ESTE cliente.
            while (true) {
                int length = dis.readInt();
                if (length > 0) {
                    byte[] data = new byte[length];
                    dis.readFully(data);
                    Image img = new Image(new ByteArrayInputStream(data));

                    BufferedImage imagen = SwingFXUtils.fromFXImage(img,null);
                    Image imgFinal = SwingFXUtils.toFXImage(imagen,null);


                    if (contador < plantilla.getGaleria().size()){
                        fotos.add(imgFinal);
                    } else {
                        fotos.add(imgFinal);
                        plantilla.setGaleria(fotos);
                        Platform.runLater(() -> {
                            activarDesactivarNodo.textOff(plantilla.getGaleria());
                            ExportarPlantilla.guardarComoPNG(plantilla);
                            activarDesactivarNodo.textOn(plantilla.getGaleria());
                        });
                        fotos.clear();
                        contador = 0;
                    }
                    contador++;
                }
            }
        } catch (EOFException e) {
            System.out.println("Se desconecto el cliente");
        } catch (Exception e) {
            System.err.println("Error durante la comunicación con el cliente: " + e.getMessage());
        } finally {
            this.commandWriter = null; // Limpiamos el writer al desconectar.
            try {
                clientSocket.close(); // Nos aseguramos de cerrar el socket del cliente.
            } catch (Exception e) {
                // Ignorar errores al cerrar.
            }
        }
    }


    public void stopServer() {
        System.out.println("Intentando detener el servidor...");
        try {
            // Interrumpir el hilo por si está en una espera
            if (serverThread != null) {
                serverThread.interrupt();
            }
            // Cerrar el server socket para no aceptar más conexiones
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Servidor detenido correctamente.");
        } catch (Exception e) {
            System.err.println("Error al detener el servidor: " + e.getMessage());
            throw  new RuntimeException();
        }
    }

    public void sendCommand(String jsonString) {
        if (commandWriter != null) {
            new Thread(() -> { // Es buena práctica no hacer operaciones de red en el hilo de UI
                commandWriter.println(jsonString);
                System.out.println("Configuracion Enviada");
            }).start();
        } else {
            System.out.println("No se puede enviar el comando, no hay cliente conectado.");
        }
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

}
