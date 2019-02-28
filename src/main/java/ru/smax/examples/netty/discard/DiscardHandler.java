package ru.smax.examples.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Discard data
        final ByteBuf in = (ByteBuf) msg;

        if (log.isDebugEnabled()) {
            log.debug(in.toString(CharsetUtil.US_ASCII).trim());
        }

        in.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getLocalizedMessage());
        ctx.close();
    }
}
