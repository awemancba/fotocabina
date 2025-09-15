package org.francalderon.app.fotocabina.services;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebCamServiceSocket {
    private final int port;
    private ImageView imageView;

    public WebCamServiceSocket(int port) {
        this.port = port;
    }

    public void starServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Servidor escuchando en puerto " + port);
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());
                try (DataInputStream dis = new DataInputStream(client.getInputStream())) {
                    while (true) {
                        int length = dis.readInt();
                        byte[] data = new byte[length];
                        dis.readFully(data);

                        Image img = new Image(new ByteArrayInputStream(data));
                        Platform.runLater(()->imageView.setImage(img));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
