package ru.smax.socket.ssl;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import static ru.smax.socket.ssl.Config.PORT;

@Slf4j
class ServerStarter {
    public static void main(String[] args) throws InterruptedException {
        log.info("Starting new Server.Listener...");

        final Thread listener = new Listener();
        listener.start();
        listener.join();

        log.info("Shutting down Server...");
    }

    private static class Listener extends Thread {
        private static final int MAX_CONNECTIONS = 1;

        @Override
        public void run() {
            try (final ServerSocket serverSocket = createServerSocket()) {
                log.info("Listener started");

                int current = 0;
                int maxTries = 2;

                while (current < maxTries) {
                    current++;
                    log.info("Waiting for connection...");
                    handleRequest(serverSocket);
                }
            } catch (IOException e) {
                log.error("Could not create SSL ServerSocket", e);
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
                log.info("Request received");
                try (final OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {
                    outputStream.write(123);
                    outputStream.flush();
                }
                log.info("Request was processed successfully");
            } catch (SocketTimeoutException e) {
                log.error("ServerSocket timeout");
            } catch (IOException e) {
                log.error("Could not handle request", e);
            }
        }
    }
}
