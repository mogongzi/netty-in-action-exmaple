package me.ryan.netty.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PlainOIOServer {

    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);

        while (true) {
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);

            new Thread(() -> {
                OutputStream out;
                try {
                    out = clientSocket.getOutputStream();
                    out.write("Hi!\r\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        PlainOIOServer server = new PlainOIOServer();
        try {
            server.serve(8848);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
