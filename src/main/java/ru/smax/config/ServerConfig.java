package ru.smax.config;

public class ServerConfig {
    public static final String HOST = "localhost";
    public static final int PORT = 11111;

    private ServerConfig() {
        throw new AssertionError("Not for instantiation!");
    }
}
