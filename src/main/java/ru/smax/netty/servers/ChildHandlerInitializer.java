package ru.smax.netty.servers;

import static lombok.AccessLevel.PRIVATE;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = PRIVATE)
public class ChildHandlerInitializer extends ChannelInitializer<SocketChannel> {
    private final List<Class<? extends ChannelInboundHandlerAdapter>> handlerClasses;

    @SafeVarargs
    public static ChildHandlerInitializer of(Class<? extends ChannelInboundHandlerAdapter>... handlerClasses) {
        return new ChildHandlerInitializer(
                Stream.of(handlerClasses)
                      .collect(Collectors.toCollection(
                              LinkedList::new
                      ))
        );
    }

    @Override
    protected void initChannel(SocketChannel ch) throws InstantiationException, IllegalAccessException {
        for (Class<? extends ChannelInboundHandlerAdapter> handlerClass : handlerClasses) {
            ch.pipeline()
              .addLast(handlerClass.newInstance());
        }
    }
}
