package ru.smax.netty.servers.time.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnixTime {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final long epochSecond;

    @Override
    public String toString() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond),
                                       ZoneId.systemDefault())
                            .format(DATE_TIME_FORMATTER);
    }
}
