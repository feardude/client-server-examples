package ru.smax.netty.servers.time.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import ru.smax.netty.servers.time.model.UnixTime;

public class TimeDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(
                new UnixTime(in.readUnsignedInt())
        );
    }
}
