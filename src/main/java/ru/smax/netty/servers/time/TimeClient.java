package ru.smax.netty.servers.time;

import static io.netty.channel.ChannelOption.SO_KEEPALIVE;
import static ru.smax.config.ServerConfig.HOST;
import static ru.smax.config.ServerConfig.TIME_PORT;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import ru.smax.netty.servers.ChildHandlerInitializer;

@Slf4j
public class TimeClient {
    public static void main(String[] args) {
        try {
            Starter.run();
        }
        catch (Exception e) {
            log.error("Exception while running TimeClient", e);
        }
    }

    private static class Starter {
        private static final boolean KEEP_ALIVE = true;

        private static void run() throws InterruptedException {
            log.info("Starting TimeClient...");

            final EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                final Bootstrap bootstrap = getBootstrap(workerGroup);
                final ChannelFuture future = bootstrap.connect(HOST, TIME_PORT)
                                                      .sync();
                log.info("TimeClient started");

                future.channel()
                      .closeFuture()
                      .sync();
            }
            finally {
                log.info("Shutting down TimeClient...");
                workerGroup.shutdownGracefully();
            }
        }

        private static Bootstrap getBootstrap(EventLoopGroup workerGroup) {
            return new Bootstrap().group(workerGroup)
                                  .channel(NioSocketChannel.class)
                                  .handler(ChildHandlerInitializer.of(TimeClientHandler.class))
                                  .option(SO_KEEPALIVE, KEEP_ALIVE);
        }
    }
}
