package ru.smax.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import static io.netty.channel.ChannelOption.SO_BACKLOG;
import static io.netty.channel.ChannelOption.SO_KEEPALIVE;
import static ru.smax.config.ServerConfig.HOST;
import static ru.smax.config.ServerConfig.PORT;

@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        try {
            Starter.run();
        } catch (Exception e) {
            log.error("Exception while running server", e);
        }
    }

    private static class Starter {
        private static final int MAX_CONNECTIONS = 1;

        private static void run() throws InterruptedException {
            log.info("Starting NettyServer...");

            final NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
            final NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

            try {
                final ServerBootstrap serverBootstrap = getServerBootstrap(bossEventLoopGroup, workerEventLoopGroup);
                final ChannelFuture channelFuture = serverBootstrap.bind(HOST, PORT)
                                                                   .sync();
                log.info("NettyServer started");

                channelFuture.channel()
                             .closeFuture()
                             .sync();
                log.info("Shutting down NettyServer...");
            } finally {
                workerEventLoopGroup.shutdownGracefully();
                bossEventLoopGroup.shutdownGracefully();
            }
        }

        private static ServerBootstrap getServerBootstrap(NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup) {
            return new ServerBootstrap().group(bossGroup, workerGroup)
                                        .channel(NioServerSocketChannel.class)
                                        .childHandler(ChildHandlerInitializer.of(EchoHandler.class))
                                        .option(SO_BACKLOG, MAX_CONNECTIONS)
                                        .childOption(SO_KEEPALIVE, true);
        }
    }
}
