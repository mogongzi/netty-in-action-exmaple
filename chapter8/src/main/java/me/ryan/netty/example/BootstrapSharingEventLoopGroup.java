package me.ryan.netty.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class BootstrapSharingEventLoopGroup {

    public void bootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChannelInboundHandler<ByteBuffer>() {
                    ChannelFuture connectFuture;
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        Bootstrap bootstrap1 = new Bootstrap();
                        bootstrap1.channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                System.out.println("Received data.");
                            }
                        });
                        bootstrap1.group(ctx.channel().eventLoop());
                        connectFuture = bootstrap1.connect(new InetSocketAddress("www.manning.com", 80));
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuffer byteBuffer) throws Exception {
                        if (connectFuture.isDone()) {
                            System.out.println("Connection is complete.");
                        }
                    }
                });

        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound.");
                } else {
                    System.err.println("Bind attempt failed.");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
