package me.ryan.netty.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent logEvent) throws Exception {
        StringBuilder buffer = new StringBuilder();
        buffer.append(logEvent.getReceived());
        buffer.append("[");
        buffer.append(logEvent.getSource().toString());
        buffer.append("][");
        buffer.append(logEvent.getLogFile());
        buffer.append("]:");
        buffer.append(logEvent.getMsg());
        System.out.println(buffer.toString());
    }
}
