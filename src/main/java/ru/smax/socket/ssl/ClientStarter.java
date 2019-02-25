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

        final Thread clientThread = new Client();
        clientThread.start();
        clientThread.join();

        log.info("Shutting down Client...");
    }

    private static class Client extends Thread {
        @Override
        public void run() {
            log.info("Client started");
            try (final SSLSocket socket = createSslSocket()) {
                socket.getInputStream();
            } catch (IOException e) {
                log.error("Could not create SSL Socket", e);
            }
        }

        private SSLSocket createSslSocket() throws IOException {
            return configuredSslSocket(
                    (SSLSocket) SSLSocketFactory.getDefault()
                                                .createSocket(HOST, PORT)
            );
        }

        private SSLSocket configuredSslSocket(SSLSocket socket) throws IOException {
            socket.startHandshake();
            log.info("Handshake was successful");
            return socket;
        }
    }
}
