package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

public class DummyChannelHandlerContext extends AbstractChannelHandlerContext {
    public static ChannelHandlerContext DUMMY_INSTANCE = new DummyChannelHandlerContext(null, null, null, null);

    DummyChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name, Class<? extends ChannelHandler> handlerClass) {
        super(pipeline, executor, name, handlerClass);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }
}
