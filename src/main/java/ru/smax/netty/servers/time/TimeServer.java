package ru.smax.netty.servers.time;

import static io.netty.channel.ChannelOption.SO_BACKLOG;
import static io.netty.channel.ChannelOption.SO_KEEPALIVE;
import static ru.smax.config.ServerConfig.HOST;
import static ru.smax.config.ServerConfig.TIME_PORT;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import ru.smax.netty.servers.ChildHandlerInitializer;

@Slf4j
public class TimeServer {
    public static void main(String[] args) {
        try {
            Starter.run();
        }
        catch (Exception e) {
            log.error("Exception while running TimeServer", e);
        }
    }

    private static class Starter {
        private static final int MAX_CONNECTIONS = 1;
        private static final boolean KEEP_ALIVE = true;

        private static void run() throws InterruptedException {
            log.info("Starting TimeServer...");

            final NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
            final NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

            try {
                final ServerBootstrap serverBootstrap = getServerBootstrap(bossEventLoopGroup, workerEventLoopGroup);
                final ChannelFuture channelFuture = serverBootstrap.bind(HOST, TIME_PORT)
                                                                   .sync();
                log.info("TimeServer started");

                channelFuture.channel()
                             .closeFuture()
                             .sync();
            }
            finally {
                log.info("Shutting down TimeServer...");
                workerEventLoopGroup.shutdownGracefully();
                bossEventLoopGroup.shutdownGracefully();
            }
        }

        private static ServerBootstrap getServerBootstrap(NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup) {
            return new ServerBootstrap().group(bossGroup, workerGroup)
                                        .channel(NioServerSocketChannel.class)
                                        .childHandler(ChildHandlerInitializer.of(TimeHandler.class))
                                        .option(SO_BACKLOG, MAX_CONNECTIONS)
                                        .childOption(SO_KEEPALIVE, KEEP_ALIVE);
        }
    }
}
