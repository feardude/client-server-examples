package ru.smax.examples.netty.time.client;

import static io.netty.channel.ChannelOption.SO_KEEPALIVE;
import static ru.smax.examples.config.ServerConfig.HOST;
import static ru.smax.examples.config.ServerConfig.PORT;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import ru.smax.examples.netty.ChildHandlerInitializer;

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
                final ChannelFuture future = bootstrap.connect(HOST, PORT)
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
                                  .handler(ChildHandlerInitializer.of(TimeDecoder.class, TimeClientHandler.class))
                                  .option(SO_KEEPALIVE, KEEP_ALIVE);
        }
    }
}
