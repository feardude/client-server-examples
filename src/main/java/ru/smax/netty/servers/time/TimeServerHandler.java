package ru.smax.netty.servers.time;

import static io.netty.channel.ChannelFutureListener.CLOSE;
import static java.time.Instant.EPOCH;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private static final int INTEGER_BYTE_SIZE = 4;

    private static int currentTime() {
        final int epochSecond = (int) EPOCH
                .plusMillis(System.currentTimeMillis())
                .getEpochSecond();
        log.info("Current epoch time = {} seconds", epochSecond);
        return epochSecond;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final ByteBuf time = ctx.alloc().buffer(INTEGER_BYTE_SIZE);
        time.writeInt(currentTime());

        ctx.writeAndFlush(time)
           .addListener(CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.toString());
        ctx.close();
    }
}
