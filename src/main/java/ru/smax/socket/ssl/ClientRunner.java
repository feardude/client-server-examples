package ru.smax.socket.ssl;

public class ClientRunner {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Client...");

        final ClientThread clientThread = new ClientThread();
        clientThread.start();
        clientThread.join();

        System.out.println("Shutting down Client...");
    }

    private static class ClientThread extends Thread {
        @Override
        public void run() {
            System.out.println("wololo");
        }
    }
}
