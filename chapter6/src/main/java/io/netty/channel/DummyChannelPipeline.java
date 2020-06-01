package io.netty.channel;

public class DummyChannelPipeline extends DefaultChannelPipeline {
    public static final ChannelPipeline DUMMY_INSTANCE = new DummyChannelPipeline(null);
    protected DummyChannelPipeline(Channel channel) {
        super(channel);
    }
}
