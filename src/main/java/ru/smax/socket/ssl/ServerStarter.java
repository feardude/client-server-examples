package ru.smax.socket.ssl;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import static ru.smax.socket.ssl.Config.PORT;

class ServerStarter {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting new Server.Listener...");

        final Thread listener = new Listener();
        listener.start();
        listener.join();

        System.out.println("Shutting down Server...");
    }

    private static class Listener extends Thread {
        private static final int MAX_CONNECTIONS = 1;

        @Override
        public void run() {
            try (final ServerSocket serverSocket = createServerSocket()) {
                System.out.println("Listener started");

                int current = 0;
                int maxTries = 2;

                while (current < maxTries) {
                    current++;
                    System.out.println("Waiting for connection...");
                    handleRequest(serverSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ServerSocket createServerSocket() throws IOException {
            return configuredSocket(
                    SSLServerSocketFactory.getDefault()
                                          .createServerSocket(PORT, MAX_CONNECTIONS)
            );
        }

        private ServerSocket configuredSocket(ServerSocket serverSocket) throws SocketException {
            serverSocket.setSoTimeout(30_000);
            return serverSocket;
        }

        private void handleRequest(ServerSocket serverSocket) {
            try (final Socket socket = serverSocket.accept()) {
                System.out.println("Request received");
                try (final OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {
                    outputStream.write(123);
                    outputStream.flush();
                }
                System.out.println("Request was processed successfully");
            } catch (SocketTimeoutException e) {
                System.out.println("ServerSocket timeout");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
