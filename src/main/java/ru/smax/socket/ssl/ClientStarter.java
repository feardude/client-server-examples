package ru.smax.socket.ssl;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

import static ru.smax.socket.ssl.Config.HOST;
import static ru.smax.socket.ssl.Config.PORT;

@Slf4j
public class ClientStarter {
    public static void main(String[] args) throws Exception {
        log.info("Starting Client...");

        final ClientThread clientThread = new ClientThread();
        clientThread.start();
        clientThread.join();

        log.info("Shutting down Client...");
    }

    private static class ClientThread extends Thread {
        @Override
        public void run() {
            log.info("Client thread started");
            try (final SSLSocket socket = createSslSocket()) {
                socket.startHandshake();
                log.info("Socket connected");
                socket.getInputStream();
            } catch (IOException e) {
                log.error("Could not create SSL Socket", e);
            }
        }

        private SSLSocket createSslSocket() throws IOException {
            return (SSLSocket) SSLSocketFactory.getDefault()
                                               .createSocket(HOST, PORT);
        }
    }
}
