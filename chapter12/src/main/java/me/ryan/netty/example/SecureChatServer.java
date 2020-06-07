package me.ryan.netty.example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

public class SecureChatServer extends ChatServer {
    private final SslContext sslContext;

    public SecureChatServer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
        return new SecureChatServerInitializer(channelGroup, this.sslContext);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Please give port as argument");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext sslContext = SslContextBuilder.forServer(cert.privateKey(), cert.certificate()).build();

        final SecureChatServer server = new SecureChatServer(sslContext);
        ChannelFuture future = server.start(new InetSocketAddress(port));
        System.out.println("Starting ChatServer on port: " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::destory));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
