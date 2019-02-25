package ru.smax.socket.ssl;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static ru.smax.socket.ssl.Config.PORT;

class ServerStarter {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting new Server.Listener...");

        final Thread listener = new Listener();
        listener.start();
        listener.join();

        System.out.println("Shutting down Server...");
    }

    private static class Listener extends Thread {
        private static final int MAX_CONNECTIONS = 1;
        private final ServerSocket socket;

        Listener() throws IOException {
            this.socket = SSLServerSocketFactory.getDefault()
                                                .createServerSocket(PORT, MAX_CONNECTIONS);
            this.socket.setSoTimeout(30_000);
        }

        @Override
        public void run() {
            System.out.println("Listener started");

            int current = 0;
            int maxTries = 2;

            while (current < maxTries) {
                current++;
                System.out.println("Waiting for connection...");

                try (final Socket accept = socket.accept()) {
                    System.out.println("Request received");
                    try (final OutputStream outputStream = new BufferedOutputStream(accept.getOutputStream())) {
                        outputStream.write(123);
                        outputStream.flush();
                    }
                    System.out.println("Request was processed successfully");
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timeout");
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
