package ru.smax.netty.servers.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        final ByteBuf message = (ByteBuf) msg;

        try {
            final LocalDateTime currentDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(message.readUnsignedInt()),
                    ZoneId.systemDefault()
            );
            log.info("Current date time: {}", currentDateTime.format(DATE_TIME_FORMATTER));
            ctx.close();
        }
        finally {
            message.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getLocalizedMessage());
    }
}
