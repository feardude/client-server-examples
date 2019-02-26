package ru.smax.netty.servers;

import static lombok.AccessLevel.PRIVATE;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = PRIVATE)
public class ChildHandlerInitializer<T extends ChannelHandler> extends ChannelInitializer<SocketChannel> {
    private final Class<T> handlerClass;

    public static ChildHandlerInitializer of(Class<? extends ChannelHandler> handlerClass) {
        return new ChildHandlerInitializer<>(handlerClass);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws InstantiationException, IllegalAccessException {
        ch.pipeline()
          .addLast(handlerClass.newInstance());
    }
}
