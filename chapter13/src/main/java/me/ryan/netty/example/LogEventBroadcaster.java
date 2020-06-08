package me.ryan.netty.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws Exception {
        System.out.println(this.getClass().getSimpleName() + " is running");
        Channel channel = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
                pointer = len;
            } else if (len > pointer) {
                RandomAccessFile raFile = new RandomAccessFile(file, "r");
                raFile.seek(pointer);
                String line;
                while ((line = raFile.readLine()) != null) {
                    channel.writeAndFlush(new LogEvent(null, file.getAbsolutePath(), line, -1));
                }
                pointer = raFile.getFilePointer();
                raFile.close();
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", Integer.parseInt(args[0])), new File(System.getProperty("user.home") + args[1]));
        try {
            broadcaster.run();
        } catch (Exception e) {
            broadcaster.stop();
        }
    }
}
