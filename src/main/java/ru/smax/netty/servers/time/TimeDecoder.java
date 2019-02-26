package ru.smax.netty.servers.time;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class TimeDecoder extends ReplayingDecoder<Void> {
    private static final int INTEGER_BYTE_SIZE = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(in.readBytes(INTEGER_BYTE_SIZE));
    }
}
