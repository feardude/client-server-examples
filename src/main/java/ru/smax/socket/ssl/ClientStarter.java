package ru.smax.socket.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

import static ru.smax.socket.ssl.Config.HOST;
import static ru.smax.socket.ssl.Config.PORT;

public class ClientStarter {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Client...");

        final ClientThread clientThread = new ClientThread();
        clientThread.start();
        clientThread.join();

        System.out.println("Shutting down Client...");
    }

    private static class ClientThread extends Thread {
        @Override
        public void run() {
            System.out.println("Client thread started");
            try (final SSLSocket socket = createSslSocket()) {
                socket.startHandshake();
                System.out.println("Socket connected");
                socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private SSLSocket createSslSocket() throws IOException {
            return (SSLSocket) SSLSocketFactory.getDefault()
                                               .createSocket(HOST, PORT);
        }
    }
}
