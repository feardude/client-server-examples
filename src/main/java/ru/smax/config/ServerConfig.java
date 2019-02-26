package ru.smax.config;

public class ServerConfig {
    public static final String HOST = "localhost";
    public static final int PORT = 11111;
    public static final int TIME_PORT = 37;

    private ServerConfig() {
        throw new AssertionError("Not for instantiation!");
    }
}
