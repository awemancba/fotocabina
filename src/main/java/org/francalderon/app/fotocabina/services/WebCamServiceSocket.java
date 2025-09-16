package org.francalderon.app.fotocabina.services;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.francalderon.app.fotocabina.models.Plantilla;
import org.francalderon.app.fotocabina.ui.events.foto.activarDesactivarNodo;
import org.francalderon.app.fotocabina.utils.EditorImagenes;
import org.francalderon.app.fotocabina.utils.ExportarPlantilla;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
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
    private Socket clientSocket;
    private Thread serverThread;

    public WebCamServiceSocket(int port) {
        this.port = port;
    }

    public void startServer() {
        this.serverThread = new Thread(() -> {
            int contador = 1;
            List<Image> fotos = new ArrayList<>();
            try {
                //Usamoslas variables de la clase
                this.serverSocket = new ServerSocket(port);
                System.out.println("Servidor escuchando en puerto " + port);

                this.clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                this.commandWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {
                    while (!Thread.currentThread().isInterrupted()) {
                        System.out.println("esperando....");
                        int length = dis.readInt();
                        byte[] data = new byte[length];
                        dis.readFully(data);

                        Image img = new Image(new ByteArrayInputStream(data));
                        BufferedImage imagen = SwingFXUtils.fromFXImage(img,null);
                        imagen = EditorImagenes.aplicarEspejoHorizontal(imagen);

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

                        System.out.println("Archivo cargado con exito");
                    }
                }
            } catch (SocketException e ) {
                System.out.println("El servidor ha sido detenido");;
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                System.out.println("Hilo del servidor finalizado.");
            }

        });
        this.serverThread.start();
    }

    public void stopServer() {
        System.out.println("Intentando detener el servidor...");
        try {
            // Interrumpir el hilo por si está en una espera
            if (serverThread != null) {
                serverThread.interrupt();
            }
            // Cerrar el socket del cliente si está conectado
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
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

    public void sendCommand(String command) {
        if (commandWriter != null) {
            new Thread(() -> { // Es buena práctica no hacer operaciones de red en el hilo de UI
                commandWriter.println(command);
                System.out.println("Comando enviado: " + command);
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
