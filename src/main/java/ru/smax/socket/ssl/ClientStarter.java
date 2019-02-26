package ru.smax.socket.ssl;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import static ru.smax.config.ServerConfig.HOST;
import static ru.smax.config.ServerConfig.PORT;

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
                final String uuid = UUID.randomUUID().toString() + "\n";
                socket.getOutputStream().write(uuid.getBytes());
                socket.getOutputStream().flush();

                new BufferedReader(new InputStreamReader(
                        socket.getInputStream()
                )).lines()
                  .forEach(log::info);

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
